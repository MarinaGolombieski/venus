import { Link, useRouter } from "@tanstack/react-router";
import { ArrowLeft, ShoppingCart } from "lucide-react";
import { Logo } from "./Logo";
import { useStore } from "@/lib/store";

export function AppHeader({ title, subtitle, back = true }: { title?: string; subtitle?: string; back?: boolean }) {
  const router = useRouter();
  const { cart } = useStore();
  return (
    <header className="mx-auto w-full max-w-6xl px-4 pt-5 sm:px-8">
      <div className="flex items-center justify-between">
        <Link to="/loja" aria-label="Início da loja"><Logo /></Link>
        <div className="text-center">
          {title && <h1 className="text-sm font-extrabold uppercase tracking-wide text-primary">{title}</h1>}
          {subtitle && <p className="text-xs text-muted-foreground">{subtitle}</p>}
        </div>
        <Link to="/pagamento" className="relative text-foreground" aria-label="Carrinho">
          <ShoppingCart className="h-5 w-5" />
          {cart.length > 0 && (
            <span className="absolute -right-2 -top-2 grid h-4 w-4 place-items-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground">
              {cart.length}
            </span>
          )}
        </Link>
      </div>
      {back && (
        <button onClick={() => router.history.back()} className="mt-3 text-foreground" aria-label="Voltar">
          <ArrowLeft className="h-5 w-5" />
        </button>
      )}
    </header>
  );
}

export function SideTabs() {
  const tabs = [
    { to: "/loja", label: "Início" },
    { to: "/personalizar", label: "Personalizar" },
    { to: "/pedidos", label: "Pedidos" },
     { to: "/perfil", label: "Meu Perfil" }
  ] as const;
  return (
    <nav className="fixed right-0 top-24 z-10 hidden flex-col gap-1 md:flex">
      {tabs.map((t) => (
        <Link key={t.to} to={t.to}
          className="rounded-l-md bg-secondary px-4 py-1.5 text-xs font-semibold text-secondary-foreground"
          activeProps={{ className: "rounded-l-md bg-primary px-4 py-1.5 text-xs font-semibold text-primary-foreground" }}>
          {t.label}
        </Link>
      ))}
    </nav>
  );
}
