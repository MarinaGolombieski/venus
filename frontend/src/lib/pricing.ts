// Cálculo de preço determinístico em centavos inteiros — nunca usa IA.
import { getProduct } from "@/data/products";
import { getCustomizationOption } from "@/data/customizations";

export interface CartItem {
  key: string;
  productId: string;
  size: string;
  quantity: number;
  customizations: { optionId: string; details: Record<string, string> }[];
}

export const SHIPPING_CENTS = 0; // PENDENTE: regra de frete não definida no relatório.

export function unitPriceCents(item: Pick<CartItem, "productId" | "customizations">) {
  const p = getProduct(item.productId);
  if (!p) return 0;
  const extra = item.customizations.reduce((s, c) => s + (getCustomizationOption(c.optionId)?.extraCents ?? 0), 0);
  return p.priceCents + extra;
}

export const lineTotalCents = (i: CartItem) => i.subtotalCents ?? (unitPriceCents(i) * Math.max(0, Math.floor(i.quantity)));

export function cartTotals(items: CartItem[]) {
  const subtotal = items.reduce((s, i) => s + lineTotalCents(i), 0);
  return { subtotal, shipping: SHIPPING_CENTS, total: subtotal + SHIPPING_CENTS };
}

export const brl = (cents: number) =>
  (cents / 100).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
