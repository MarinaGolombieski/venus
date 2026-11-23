// Recomendação DEMONSTRATIVA por similaridade de atributos (categoria, tecido, estampa, tom, estilo).
// Ainda não usa IA de imagem: será substituída por embeddings visuais quando o catálogo real existir.
import { PRODUCTS, type Product } from "@/data/products";

export function similarProducts(base: Product, limit = 3): Product[] {
  const score = (p: Product) =>
    (p.category === base.category ? 3 : 0) +
    (p.tags.fabric === base.tags.fabric ? 2 : 0) +
    (p.tags.pattern === base.tags.pattern ? 2 : 0) +
    (p.tags.tone === base.tags.tone ? 1 : 0) +
    (p.tags.style === base.tags.style ? 1 : 0);
  return PRODUCTS.filter((p) => p.id !== base.id)
    .map((p) => ({ p, s: score(p) }))
    .sort((a, b) => b.s - a.s)
    .slice(0, limit)
    .map((x) => x.p);
}
