import { createFileRoute, Link } from "@tanstack/react-router";
import { Check } from "lucide-react";
import { AppHeader } from "@/components/venus/AppHeader";

export const Route = createFileRoute("/sucesso")({
  validateSearch: (s: Record<string, unknown>) => ({ pedido: String(s["pedido"] ?? "") }),
  head: () => ({ meta: [
    { title: "Pagamento realizado — Vênus" }, { name: "description", content: "Seu pedido foi confirmado." },
    { property: "og:title", content: "Pagamento realizado — Vênus" }, { property: "og:description", content: "Seu pedido foi confirmado." },
  ] }),
  component: () => {
    const { pedido } = Route.useSearch();
    return (
      <div className="flex min-h-screen flex-col">
        <AppHeader title="Pagamento" back={false} />
        <main className="m-4 flex flex-1 flex-col items-center justify-center gap-4 rounded-2xl bg-secondary p-10 text-center">
          <h2 className="text-lg font-extrabold">Pagamento realizado!</h2>
          <div className="grid h-16 w-16 place-items-center rounded-full border-4 border-primary text-primary animate-in zoom-in"><Check className="h-8 w-8" /></div>
          {pedido && <p className="text-xs text-muted-foreground">Pedido nº {pedido}</p>}
          <Link to="/pedidos" className="btn-primary">Acompanhar pedido</Link>
        </main>
      </div>
    );
  },
});
