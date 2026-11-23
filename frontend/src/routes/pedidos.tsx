import { createFileRoute, Link } from "@tanstack/react-router";
import { Check, Home, Scissors, Sparkles, Truck } from "lucide-react";
import { AppHeader, SideTabs } from "@/components/venus/AppHeader";
import { brl } from "@/lib/pricing";
import { useStore, type Order } from "@/lib/store";

export const Route = createFileRoute("/pedidos")({
  head: () => ({ meta: [
    { title: "Meus pedidos — Vênus" }, { name: "description", content: "Acompanhe o status dos seus pedidos." },
    { property: "og:title", content: "Meus pedidos — Vênus" }, { property: "og:description", content: "Acompanhe o status dos seus pedidos." },
  ] }),
  component: Pedidos,
});

function steps(o: Order) {
  return o.hasCustomization
    ? [{ l: "Pedido confirmado", i: Check }, { l: "Em produção", i: Scissors }, { l: "Finalizado", i: Sparkles }, { l: "Enviado", i: Truck }, { l: "Entregue", i: Home }]
    : [{ l: "Pedido confirmado", i: Check }, { l: "Enviado", i: Truck }, { l: "Entregue", i: Home }];
}

function Pedidos() {
  const { orders } = useStore();
  return (
    <div className="min-h-screen pb-16">
      <AppHeader title="Pedidos" /><SideTabs />
      <main className="mx-auto max-w-5xl space-y-6 px-4 py-6 sm:px-8">
        {orders.length === 0 && <div className="py-16 text-center text-sm text-muted-foreground">Você ainda não fez pedidos. <Link to="/loja" className="text-primary underline">Ver peças</Link></div>}
        {orders.map((o) => (
          <section key={o.id} className="panel bg-blush">
            <p className="text-sm font-bold">Status do seu pedido nº {o.id}</p>
            <p className="text-xs text-muted-foreground">{new Date(o.createdAt).toLocaleString("pt-BR")} · {o.payment}</p>
            <ol className="mt-6 flex flex-wrap justify-around gap-6">
              {steps(o).map((s, idx) => { const done = idx === 0; const I = s.i; return (
                <li key={s.l} className="flex w-24 flex-col items-center text-center text-xs">
                  <span className={`grid h-12 w-12 place-items-center rounded-full ${done ? "bg-primary text-primary-foreground" : "bg-muted text-muted-foreground"}`}><I className="h-5 w-5" /></span>
                  <span className={`mt-2 font-bold ${done ? "text-primary" : "text-muted-foreground"}`}>{s.l}</span>
                </li>); })}
            </ol>
            <p className="mt-6 text-sm">Total: <span className="font-extrabold">{brl(o.totalCents)}</span></p>
          </section>
        ))}
      </main>
    </div>
  );
}
