import { createFileRoute, Link } from "@tanstack/react-router";
import { LogoMark } from "@/components/venus/Logo";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Vênus — Moda personalizada sob demanda" },
      { name: "description", content: "Entre na Vênus: roupas novas e seminovas, personalização e produção sob demanda." },
      { property: "og:title", content: "Vênus — Moda personalizada sob demanda" },
      { property: "og:description", content: "Roupas novas e seminovas, personalização e produção sob demanda." },
    ],
  }),
  component: Splash,
});

function Splash() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center bg-wine px-6 text-wine-foreground">
      <Link to="/login" aria-label="Entrar na Vênus" className="flex flex-col items-center gap-6 animate-in fade-in zoom-in-95 duration-700">
        <LogoMark className="h-24 w-24 text-primary" />
        <span className="font-display text-4xl font-bold tracking-wide">Vênus</span>
        <span className="btn-primary">Começar</span>
      </Link>
      <p className="absolute bottom-6 text-xs opacity-80">Produzido por <span className="font-semibold text-accent">WalkWorld</span></p>
    </main>
  );
}
