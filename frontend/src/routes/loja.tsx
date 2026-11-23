
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";
import { AppHeader, SideTabs } from "@/components/venus/AppHeader";
import { ProductCard } from "@/components/venus/ProductCard";
import {
  PRODUCTS,
  fetchProductsFromApi,
  getProductTotalStock,
  type Product,
} from "@/data/products";
import { useStore } from "@/lib/store";

export const Route = createFileRoute("/loja")({
  head: () => ({
    meta: [
      {
        title: "Catálogo — Vênus",
      },
      {
        name: "description",
        content: "Peças novas e seminovas para personalizar.",
      },
      {
        property: "og:title",
        content: "Catálogo — Vênus",
      },
      {
        property: "og:description",
        content: "Peças novas e seminovas para personalizar.",
      },
    ],
  }),
  component: Loja,
});

function Loja() {
  const { user } = useStore();

  const [products, setProducts] = useState<Product[]>(PRODUCTS);
  const [cond, setCond] = useState("Todas");
  const [q, setQ] = useState("");
  const [max, setMax] = useState(300);
  const [availability, setAvailability] = useState("Todos");

  useEffect(() => {
    void fetchProductsFromApi()
      .then((data) => {
        setProducts(data.length > 0 ? data : PRODUCTS);
      })
      .catch((e) => {
        console.warn("API indisponível; usando dados demonstrativos", e);
        setProducts(PRODUCTS);
      });
  }, []);

  const list = useMemo(
    () =>
      products.filter((p) => {
        const stock = getProductTotalStock(p);

        return (
          (cond === "Todas" || p.condition === cond) &&
          (availability === "Todos" ||
            (availability === "Em estoque" && stock > 0) ||
            (availability === "Esgotados" && stock === 0)) &&
          p.priceCents <= max * 300 &&
          p.name.toLowerCase().includes(q.toLowerCase())
        );
      }),
    [products, cond, availability, q, max]
  );

  return (
    <div className="min-h-screen pb-16">
      <AppHeader back={false} />
      <SideTabs />

      <main className="mx-auto max-w-6xl px-4 sm:px-8">
        <h2 className="mt-4 text-lg font-extrabold text-primary">
          Olá{user ? `, ${user.name}` : ""}!
        </h2>

        <p className="text-xs text-muted-foreground">
          O que vamos criar hoje?
        </p>

       
{/* Filtros */}
<div className="mt-5 flex flex-wrap items-center gap-x-8 gap-y-4">

  {/* Filtro de condição */}
  <div className="flex flex-wrap items-center gap-2">
    <span className="text-xs font-bold text-muted-foreground">
      Condição:
    </span>

    {["Todas", "Novo", "Seminovo"].map((c) => (
      <button
        key={c}
        type="button"
        onClick={() => setCond(c)}
        className={`chip ${cond === c ? "chip-active" : ""}`}
      >
        {c === "Todas" ? "Todas" : c + "s"}
      </button>
    ))}
  </div>

  {/* Filtro de disponibilidade */}
  <div className="flex flex-wrap items-center gap-2">
    <span className="text-xs font-bold text-muted-foreground">
      Estoque:
    </span>

    {["Todos", "Em estoque", "Esgotados"].map((option) => (
      <button
        key={option}
        type="button"
        onClick={() => setAvailability(option)}
        className={`chip ${
          availability === option ? "chip-active" : ""
        }`}
      >
        {option}
      </button>
    ))}
  </div>

  {/* Busca e preço */}
  <div className="flex flex-wrap items-center gap-4">
    <input
      aria-label="Buscar"
      placeholder="Buscar peça..."
      className="field w-44 py-1.5"
      value={q}
      onChange={(e) => setQ(e.target.value)}
    />

    <label className="flex items-center gap-2 text-xs">
      Até R$ {max}
      <input
        type="range"
        min={30}
        max={300}
        value={max}
        onChange={(e) => setMax(Number(e.target.value))}
        className="accent-primary"
      />
    </label>
  </div>

</div>


        {/* Produtos encontrados */}
        {list.length === 0 ? (
          <p className="mt-16 text-center text-sm text-muted-foreground">
            Nenhuma peça encontrada com esses filtros.
          </p>
        ) : (
          <div className="mt-8 grid grid-cols-2 gap-6 sm:grid-cols-3 lg:grid-cols-4">
            {list.map((p) => (
              <ProductCard key={p.id} product={p} />
            ))}
          </div>
        )}

        <p className="mt-10 text-center text-[11px] text-muted-foreground">
          Fotos demonstrativas — serão substituídas pelas fotos reais das peças.
        </p>
      </main>
    </div>
  );
}
