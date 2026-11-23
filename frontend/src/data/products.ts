import sueter from "@/assets/p-sueter.jpg";
import camisa from "@/assets/p-camisa.jpg";
import regata from "@/assets/p-regata.jpg";
import blusa from "@/assets/p-blusa.jpg";
import { productsApi, productImageUrl, type BackendProduct } from "@/services/venusApi";

export type Size = "PP" | "P" | "M" | "G" | "GG";
export const ALL_SIZES: Size[] = ["PP", "P", "M", "G", "GG"];

export interface Product {
  id: string;
  backendId?: number;
  name: string;
  description: string;
  category: "Blusas" | "Camisas" | "Suéteres" | "Regatas";
  priceCents: number;
  stock: Record<Size, number>;
  image: string;
  color: string;
  condition: "Novo" | "Seminovo";
  /** Atributos usados pelo mecanismo de recomendação */
  tags: { style: string; fabric: string; pattern: string; tone: string };
  customizable: boolean;

  stockTotal?: number;
}

// FOTOS DEMONSTRATIVAS geradas por IA — substituir pelas fotos reais.
export const PRODUCTS: Product[] = [
  {
    id: "sueter-listrado", name: "Suéter Listrado Off-White", category: "Suéteres",
    description: "Suéter de tricô em algodão com listras pretas, gola careca e punhos canelados. Peça atemporal produzida sob demanda.",
    priceCents: 8990, image: sueter, color: "Off-white", condition: "Novo",
    stock: { PP: 2, P: 5, M: 4, G: 0, GG: 1 },
    tags: { style: "casual", fabric: "tricô", pattern: "listrado", tone: "neutro" }, customizable: false,
  },
  {
    id: "camisa-branca", name: "Camisa Social Branca", category: "Camisas",
    description: "Camisa social branca confeccionada em tecido de 97% algodão e 3% elastano, unindo conforto, durabilidade e praticidade. Possui gola tradicional e fechamento por botões.",
    priceCents: 6990, image: camisa, color: "Branco", condition: "Novo",
    stock: { PP: 3, P: 6, M: 6, G: 4, GG: 0 },
    tags: { style: "social", fabric: "algodão", pattern: "liso", tone: "neutro" }, customizable: false,
  },
  {
    id: "regata-croche", name: "Regata Crochê Amarela", category: "Regatas",
    description: "Crochet Tank Top amarelo manteiga confeccionado em tricô, com toque leve e confortável. Possui modelagem sem mangas e acabamento ondulado na barra.",
    priceCents: 5490, image: regata, color: "Amarelo", condition: "Seminovo",
    stock: { PP: 0, P: 2, M: 1, G: 1, GG: 0 },
    tags: { style: "casual", fabric: "tricô", pattern: "texturizado", tone: "claro" }, customizable: false,
  },
  {
    id: "blusa-rosa", name: "Blusa Básica Rosê", category: "Blusas",
    description: "Camiseta básica confeccionada em tecido leve e confortável, com composição de 100% algodão. Possui toque macio e é ideal para o dia a dia.",
    priceCents: 3990, image: blusa, color: "Rosa", condition: "Novo",
    stock: { PP: 4, P: 8, M: 8, G: 5, GG: 3 },
    tags: { style: "casual", fabric: "algodão", pattern: "liso", tone: "claro" }, customizable: false,
  },
];

export const getProduct = (id: string) => PRODUCTS.find((p) => p.id === id);

export function mapBackendProduct(p: BackendProduct): Product {
  const fallback = [sueter, camisa, regata, blusa][Math.max(0, (p.id - 1) % 4)] ?? sueter;
  return {
    id: String(p.id), backendId: p.id, name: p.nome, description: p.descricao ?? "",
    category: "Blusas", priceCents: Math.round(Number(p.preco) * 100),
    stock: { PP: p.estoque, P: p.estoque, M: p.estoque, G: p.estoque, GG: p.estoque },
    image: p.imagemIds?.[0] ? productImageUrl(p.imagemIds[0]) : fallback,
    color: "", condition: p.tipo === "SEMINOVO" ? "Seminovo" : "Novo",
    tags: { style: "", fabric: "", pattern: "", tone: "" },
    customizable: p.tipo === "SEMINOVO",

    stockTotal: p.estoque,
  };
}

export async function fetchProductsFromApi(): Promise<Product[]> {
  const data = await productsApi.list();
  return data.map(mapBackendProduct);
}

export async function fetchProductFromApi(id: string): Promise<Product> {
  return mapBackendProduct(await productsApi.get(id));
}



export function getProductTotalStock(product: Product): number {
  if (typeof product.stockTotal === "number") {
    return product.stockTotal;
  }

  return Object.values(product.stock).reduce(
    (total, quantity) => total + quantity,
    0
  );
}