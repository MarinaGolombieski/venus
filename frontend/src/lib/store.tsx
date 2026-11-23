
import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

import {
  cartApi,
  customizationApi,
  orderApi,
  paymentApi,
  type BackendCart,
  type BackendCartItem,
  type BackendUser,
  type PersonalizationPayload,
} from "@/services/venusApi";

export interface CartItem {
  key: string;
  backendItemId?: number;
  cartId?: number;
  productId: string;
  size: string;
  quantity: number;
  customizations: {
    optionId: string;
    details: Record<string, string>;
    backendId?: number;
  }[];
  subtotalCents?: number;
}

export interface Order {
  id: string;
  createdAt: string;
  items: CartItem[];
  totalCents: number;
  payment: "Pix" | "Cartão" | "Boleto";
  status: "Pendente" | "Pago" | "Em produção" | "Enviado" | "Entregue";
  hasCustomization: boolean;
}

interface User {
  idUsuario: number;
  name: string;
  email: string;
  telefone: string;
  fotoPerfil: string | null;
  cep: string;
  rua: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
}

interface Store {
  user: User | null;
  setUser: (u: User | null) => void;
  cart: CartItem[];
  cartId: number | null;
  refreshCart: () => Promise<void>;
  addToCart: (i: Omit<CartItem, "key">) => Promise<CartItem>;
  addCustomization: (
    itemId: number,
    payload: Omit<PersonalizationPayload, "itemCarrinhoId">
  ) => Promise<void>;
  updateQty: (key: string, q: number) => Promise<void>;
  removeItem: (key: string) => Promise<void>;
  clearCart: () => void;
  orders: Order[];
  placeOrder: (payment: Order["payment"]) => Promise<Order>;
}

const Ctx = createContext<Store | null>(null);

const load = <T,>(k: string, d: T): T => {
  try {
    const v = localStorage.getItem(k);
    return v ? (JSON.parse(v) as T) : d;
  } catch {
    return d;
  }
};

const paymentEnum = (p: Order["payment"]) =>
  p === "Cartão" ? "CARTAO" : p === "Boleto" ? "BOLETO" : "PIX";

const statusLabel = (s: string): Order["status"] => {
  const statuses: Record<string, Order["status"]> = {
    PENDENTE: "Pendente",
    PAGO: "Pago",
    ENVIADO: "Enviado",
    ENTREGUE: "Entregue",
    CANCELADO: "Pendente",
  };

  return statuses[s] ?? "Pendente";
};

// Função corrigida para acessar as propriedades com colchetes.

function detailsFromCustomization(
  c: BackendCartItem["personalizacoes"][number]
): Record<string, string> {
  const details: Record<string, string> = {};

  if (typeof c["desenho"] === "string" && c["desenho"]) {
    details["desenho"] = c["desenho"];
  }

  if (typeof c["cor"] === "string" && c["cor"]) {
    details["cor"] = c["cor"];
  }

  if (typeof c["tamanhoBordado"] === "string" && c["tamanhoBordado"]) {
    details["tamanho"] = c["tamanhoBordado"];
  }

  if (typeof c["localAplicacao"] === "string" && c["localAplicacao"]) {
    details["posicao"] = c["localAplicacao"];
  }

  if (typeof c["texto"] === "string" && c["texto"]) {
    details["texto"] = c["texto"];
  }

  if (typeof c["fonte"] === "string" && c["fonte"]) {
    details["fonte"] = c["fonte"];
  }

  if (typeof c["tipoRenda"] === "string" && c["tipoRenda"]) {
    details["tipoRenda"] = c["tipoRenda"];
  }

  if (typeof c["larguraRenda"] === "string" && c["larguraRenda"]) {
    details["larguraRenda"] = c["larguraRenda"];
  }

  if (typeof c["corTingimento"] === "string" && c["corTingimento"]) {
    details["corTingimento"] = c["corTingimento"];
  }

  if (typeof c["tipoManga"] === "string" && c["tipoManga"]) {
    details["tipoManga"] = c["tipoManga"];
  }

  return details;
}


function mapCart(data: BackendCart): CartItem[] {
  return data.itens.map((i) => ({
    key: String(i.idItemCarrinho),
    backendItemId: i.idItemCarrinho,
    cartId: data.idCarrinho,
    productId: String(i.produtoId),
    size: (i as BackendCartItem & { tamanho?: string }).tamanho ?? "M",
    quantity: i.quantidade,
    customizations: i.personalizacoes.map((c) => ({
      optionId: c.tipo.toLowerCase(),
      backendId: c.idPersonalizacao,
      details: detailsFromCustomization(c),
    })),
    subtotalCents: Math.round(Number(i.subtotal) * 100),
  }));
}

export function StoreProvider({ children }: { children: ReactNode }) {
  const [user, setUserState] = useState<User | null>(() =>
    load<User | null>("venus.user", null)
  );

  const [cart, setCart] = useState<CartItem[]>([]);
  const [cartId, setCartId] = useState<number | null>(null);

  const [orders, setOrders] = useState<Order[]>(() =>
    load<Order[]>("venus.orders", [])
  );

  const refreshCart = async (): Promise<void> => {
    if (user === null) {
      setCart([]);
      setCartId(null);
      return;
    }

    try {
      let data: BackendCart;

      try {
        data = await cartApi.getByUser(user.idUsuario);
      } catch {
        data = await cartApi.create(user.idUsuario);
      }

      setCartId(data.idCarrinho);
      setCart(mapCart(data));
    } catch (e) {
      console.error("Não foi possível carregar o carrinho", e);
    }
  };

  useEffect(() => {
    if (user !== null) {
      void refreshCart();
    } else {
      setCart([]);
      setCartId(null);
    }
  }, [user?.idUsuario]);

  useEffect(() => {
    localStorage.setItem("venus.user", JSON.stringify(user));
  }, [user]);

  useEffect(() => {
    localStorage.setItem("venus.orders", JSON.stringify(orders));
  }, [orders]);

  const setUser = (u: User | null): void => {
    setUserState(u);
  };

  const addToCart = async (
    i: Omit<CartItem, "key">
  ): Promise<CartItem> => {
    if (user === null) {
      throw new Error("Faça login para adicionar itens ao carrinho.");
    }

    let cid = cartId;

    if (cid === null) {
      const c = await cartApi
        .create(user.idUsuario)
        .catch(() => cartApi.getByUser(user.idUsuario));

      cid = c.idCarrinho;
      setCartId(cid);
    }

    const produtoId = Number(i.productId);

    if (!Number.isInteger(produtoId) || produtoId <= 0) {
      throw new Error(
        "Esta peça é demonstrativa e ainda não está cadastrada no banco de dados."
      );
    }

    const created = await cartApi.addItem(
      cid,
      produtoId,
      i.quantity,
      i.size
    );

    await refreshCart();

    return {
      ...i,
      key: String(created.idItemCarrinho),
      backendItemId: created.idItemCarrinho,
      cartId: cid,
    };
  };

  const addCustomization = async (
    itemId: number,
    payload: Omit<PersonalizationPayload, "itemCarrinhoId">
  ): Promise<void> => {
    await customizationApi.add({
      ...payload,
      itemCarrinhoId: itemId,
    });

    await refreshCart();
  };

  const updateQty = async (key: string, q: number): Promise<void> => {
    const item = cart.find((i) => i.key === key);

    if (item === undefined || item.backendItemId === undefined) {
      return;
    }

    await cartApi.updateItem(item.backendItemId, Math.max(1, q));
    await refreshCart();
  };

  const removeItem = async (key: string): Promise<void> => {
    const item = cart.find((i) => i.key === key);

    if (item === undefined || item.backendItemId === undefined) {
      return;
    }

    await cartApi.removeItem(item.backendItemId);
    await refreshCart();
  };

  const clearCart = (): void => {
    setCart([]);
  };

  const placeOrder = async (
    payment: Order["payment"]
  ): Promise<Order> => {
    if (user === null) {
      throw new Error("Faça login para finalizar sua compra.");
    }

    const camposEndereco = [
      user.cep,
      user.rua,
      user.numero,
      user.bairro,
      user.cidade,
      user.estado,
    ];

    const enderecoCompleto = camposEndereco.every(
      (campo) => typeof campo === "string" && campo.trim().length > 0
    );

    if (enderecoCompleto === false) {
      throw new Error(
        "Seu endereço de entrega está incompleto. Atualize seu endereço no perfil antes de finalizar a compra."
      );
    }

    if (cartId === null) {
      throw new Error("Carrinho não encontrado.");
    }

    const checkout = await orderApi.checkout(cartId);

    await paymentApi.create(
      checkout.idPedido,
      paymentEnum(payment)
    );

    const order: Order = {
      id: String(checkout.idPedido),
      createdAt: new Date().toISOString(),
      items: cart,
      totalCents: Math.round(Number(checkout.valorTotal) * 100),
      payment,
      status: statusLabel(checkout.status),
      hasCustomization: cart.some(
        (i) => i.customizations.length > 0
      ),
    };

    setOrders((os) => [order, ...os]);

    await refreshCart();

    return order;
  };

  const value: Store = {
    user,
    setUser,
    cart,
    cartId,
    refreshCart,
    addToCart,
    addCustomization,
    updateQty,
    removeItem,
    clearCart,
    orders,
    placeOrder,
  };

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useStore(): Store {
  const s = useContext(Ctx);

  if (s === null) {
    throw new Error("useStore fora do StoreProvider");
  }

  return s;
}

export type AuthUser = User;

export function mapBackendUser(u: BackendUser): User {
  return {
    idUsuario: u.idUsuario,
    name: u.nome || "",
    email: u.username,
    telefone: u.telefone || "",
    fotoPerfil: u.fotoPerfil || null,
    cep: u.cep || "",
    rua: u.rua || "",
    numero: u.numero || "",
    complemento: u.complemento || "",
    bairro: u.bairro || "",
    cidade: u.cidade || "",
    estado: u.estado || "",
  };
}
