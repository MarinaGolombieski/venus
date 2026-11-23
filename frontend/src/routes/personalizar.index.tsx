import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { AppHeader, SideTabs } from "@/components/venus/AppHeader";
import { ProductCard } from "@/components/venus/ProductCard";
import { PRODUCTS, fetchProductsFromApi, type Product } from "@/data/products";

export const Route = createFileRoute("/personalizar/")({
  head: () => ({ meta: [
    { title: "Personalize sua peça — Vênus" }, { name: "description", content: "Escolha uma peça seminova compatível para personalizar." },
  ] }),
  component: PersonalizarIndex,
});

function PersonalizarIndex() {
  const [products, setProducts] = useState<Product[]>(PRODUCTS.filter((p) => p.customizable));
  useEffect(() => { void fetchProductsFromApi().then((ps) => setProducts(ps.filter((p) => p.customizable))).catch(() => undefined); }, []);
  return (
    <div className="min-h-screen pb-16">
      <AppHeader title="Personalize sua peça" subtitle="Escolha uma peça seminova compatível" /><SideTabs />
      <main className="mx-auto grid max-w-6xl grid-cols-2 gap-6 px-4 py-8 sm:grid-cols-3 sm:px-8 lg:grid-cols-4">
        {products.map((p) => <ProductCard key={p.id} product={p} />)}
      </main>
    </div>
  );
}
