
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { MapPin, Minus, Plus, QrCode, Trash2 } from "lucide-react";
import { AppHeader } from "@/components/venus/AppHeader";
import { fetchProductFromApi, getProduct, type Product } from "@/data/products";
import { getCustomizationOption } from "@/data/customizations";
import { brl, cartTotals, lineTotalCents } from "@/lib/pricing";
import { useStore, type Order } from "@/lib/store";

export const Route = createFileRoute("/pagamento")({
  head: () => ({
    meta: [
      { title: "Pagamento — Vênus" },
      { name: "description", content: "Revise seu pedido e finalize a compra." },
      { property: "og:title", content: "Pagamento — Vênus" },
      { property: "og:description", content: "Revise seu pedido e finalize a compra." },
    ],
  }),
  component: Pagamento,
});

function Pagamento() {
  const { user, cart, updateQty, removeItem, placeOrder } = useStore();
  const nav = useNavigate();

  const [products, setProducts] = useState<Record<string, Product>>({});

  useEffect(() => {
    for (const item of cart) {
      void fetchProductFromApi(item.productId)
        .then((p) =>
          setProducts((old) => ({
            ...old,
            [item.productId]: p,
          }))
        )
        .catch(() => undefined);
    }
  }, [cart]);

  const [pay, setPay] = useState<Order["payment"]>("Pix");

  const [card, setCard] = useState({
    numero: "",
    nome: "",
    validade: "",
    cvv: "",
  });

  const [erro, setErro] = useState("");

  const t = cartTotals(cart);

  const hasCompleteAddress =
    !!user &&
    [
      user.cep,
      user.rua,
      user.numero,
      user.bairro,
      user.cidade,
      user.estado,
    ].every((campo) => typeof campo === "string" && campo.trim().length > 0);

  const confirm = async () => {
    setErro("");

    if (!hasCompleteAddress) {
      setErro(
        "Complete seu endereço de entrega no perfil antes de finalizar a compra."
      );
      return;
    }

    if (
      pay === "Cartão" &&
      (
        card.numero.replace(/\D/g, "").length < 13 ||
        !card.nome.trim() ||
        !/^\d{2}\/\d{2}$/.test(card.validade) ||
        card.cvv.length < 3
      )
    ) {
      setErro("Confira os dados do cartão.");
      return;
    }

    try {
      const o = await placeOrder(pay);
      nav({ to: "/sucesso", search: { pedido: o.id } });
    } catch (e) {
      setErro(
        e instanceof Error
          ? e.message
          : "Não foi possível finalizar o pedido."
      );
    }
  };

  return (
    <div className="flex min-h-screen flex-col">
      <AppHeader title="Pagamento" />

      {cart.length === 0 ? (
        <div className="flex flex-1 flex-col items-center justify-center gap-4 p-10 text-center">
          <p className="text-sm text-muted-foreground">
            Seu carrinho está vazio.
          </p>
          <Link to="/loja" className="btn-primary">
            Ver peças
          </Link>
        </div>
      ) : (
        <main className="mx-auto grid w-full max-w-6xl flex-1 gap-6 px-4 py-6 sm:px-8 md:grid-cols-[1fr_1.4fr]">
          <div className="space-y-4">
            <div className="panel bg-blush">
              <p className="mb-3 text-center text-sm font-bold text-primary">
                Revisão do Pedido
              </p>

              <ul className="space-y-3">
                {cart.map(
                  (i: ReturnType<typeof useStore>["cart"][number]) => {
                    const p =
                      products[i.productId] ?? getProduct(i.productId);

                    if (!p) return null;

                    return (
                      <li
                        key={i.key}
                        className="flex gap-3 rounded-lg bg-card p-2"
                      >
                        <img
                          src={p.image}
                          alt={p.name}
                          width={64}
                          height={80}
                          className="h-20 w-16 rounded object-cover"
                        />

                        <div className="flex-1 text-xs">
                          <p className="font-bold">
                            {p.name} · {i.size}
                          </p>

                          {i.customizations.map((c) => (
                            <p
                              key={c.optionId}
                              className="text-muted-foreground"
                            >
                              + {getCustomizationOption(c.optionId)?.name}:{" "}
                              {Object.values(c.details).join(", ")}
                            </p>
                          ))}

                          <div className="mt-1 flex items-center gap-2">
                            <button
                              aria-label="Diminuir"
                              onClick={() =>
                                updateQty(i.key, i.quantity - 1)
                              }
                            >
                              <Minus className="h-3 w-3" />
                            </button>

                            <span className="font-bold">{i.quantity}</span>

                            <button
                              aria-label="Aumentar"
                              onClick={() =>
                                updateQty(i.key, i.quantity + 1)
                              }
                            >
                              <Plus className="h-3 w-3" />
                            </button>

                            <button
                              aria-label="Remover"
                              className="ml-auto text-destructive"
                              onClick={() => removeItem(i.key)}
                            >
                              <Trash2 className="h-3.5 w-3.5" />
                            </button>
                          </div>
                        </div>

                        <p className="text-xs font-bold text-primary">
                          {brl(lineTotalCents(i))}
                        </p>
                      </li>
                    );
                  }
                )}
              </ul>
            </div>

            <div className="panel flex gap-3 bg-blush text-xs">
              <MapPin className="h-5 w-5 shrink-0 text-primary" />

              <div className="min-w-0 flex-1">
                <p className="mb-2 font-bold">Endereço de Entrega</p>

                {hasCompleteAddress && user ? (
                  <>
                    <p>
                      {user.rua}, {user.numero}
                      {user.complemento
                        ? ` - ${user.complemento}`
                        : ""}
                    </p>
                    <p>{user.bairro}</p>
                    <p>
                      {user.cidade}/{user.estado}
                    </p>
                    <p>CEP: {user.cep}</p>

                    <Link
                      to="/perfil"
                      className="mt-2 inline-block font-semibold text-primary underline"
                    >
                      Editar endereço
                    </Link>
                  </>
                ) : (
                  <>
                    <p className="text-muted-foreground">
                      Você precisa cadastrar todos os campos obrigatórios do
                      endereço para finalizar sua compra.
                    </p>

                    <Link
                      to="/perfil"
                      className="mt-2 inline-block font-semibold text-primary underline"
                    >
                      Cadastrar endereço no perfil
                    </Link>
                  </>
                )}
              </div>
            </div>
          </div>

          <div className="space-y-4">
            <p className="text-sm font-bold">Formas de Pagamento</p>

            {(["Pix", "Cartão", "Boleto"] as const).map((m) => (
              <button
                key={m}
                onClick={() => {
                  setPay(m);
                  setErro("");
                }}
                className={`flex w-full items-center gap-3 rounded-xl border p-3 text-left text-sm ${
                  pay === m ? "border-primary bg-blush" : ""
                }`}
              >
                <QrCode className="h-5 w-5 text-primary" />

                <span>
                  <span className="font-bold">
                    {m === "Cartão"
                      ? "Cartão de Crédito / Débito"
                      : m}
                  </span>
                  <span className="block text-xs text-muted-foreground">
                    {m === "Pix"
                      ? "Pague rápido usando QR code ou chave"
                      : m === "Boleto"
                        ? "Compensação em até 3 dias úteis"
                        : "Visa, Mastercard, Elo"}
                  </span>
                </span>
              </button>
            ))}

            {pay === "Cartão" && (
              <div className="panel grid grid-cols-2 gap-3 bg-blush">
                <input
                  className="field col-span-2 bg-card"
                  placeholder="Número do cartão"
                  inputMode="numeric"
                  value={card.numero}
                  onChange={(e) =>
                    setCard({ ...card, numero: e.target.value })
                  }
                />

                <input
                  className="field col-span-2 bg-card"
                  placeholder="Nome no cartão"
                  value={card.nome}
                  onChange={(e) =>
                    setCard({ ...card, nome: e.target.value })
                  }
                />

                <input
                  className="field bg-card"
                  placeholder="Validade (MM/AA)"
                  value={card.validade}
                  onChange={(e) =>
                    setCard({ ...card, validade: e.target.value })
                  }
                />

                <input
                  className="field bg-card"
                  placeholder="CVV"
                  inputMode="numeric"
                  maxLength={4}
                  value={card.cvv}
                  onChange={(e) =>
                    setCard({ ...card, cvv: e.target.value })
                  }
                />
              </div>
            )}

            <p className="rounded-lg bg-muted p-3 text-[11px] text-muted-foreground">
              Ambiente de teste: nenhuma cobrança real é feita até o serviço
              de pagamento ser conectado.
            </p>
          </div>
        </main>
      )}

      {cart.length > 0 && (
        <footer className="sticky bottom-0 bg-secondary py-4 text-center">
          <p className="text-xs">
            Subtotal {brl(t.subtotal)} · Frete{" "}
            {t.shipping === 0 ? "Grátis" : brl(t.shipping)}
          </p>

          <p className="text-sm font-extrabold">
            Total: {brl(t.total)}
          </p>

          {erro && (
            <p className="mx-auto max-w-xl px-4 text-xs font-semibold text-destructive">
              {erro}
            </p>
          )}

          <button
            onClick={() => void confirm()}
            className="btn-primary mt-2"
          >
            Confirmar pagamento
          </button>
        </footer>
      )}
    </div>
  );
}
