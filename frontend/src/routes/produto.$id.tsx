
import {
  createFileRoute,
  Link,
  notFound,
  useNavigate,
} from "@tanstack/react-router";
import { useEffect, useState } from "react";

import { AppHeader, SideTabs } from "@/components/venus/AppHeader";
import { ProductCard } from "@/components/venus/ProductCard";

import {
  ALL_SIZES,
  fetchProductFromApi,
  getProduct,
  getProductTotalStock,
  PRODUCTS,
  type Product,
  type Size,
} from "@/data/products";

import { recommendationApi } from "@/services/venusApi";
import { brl } from "@/lib/pricing";
import { useStore } from "@/lib/store";

type ApiProduct = {
  id: number;
  nome: string;
  descricao?: string | null;
  preco: number | string;
  estoque: number;
  tipo: string;
  estado?: string;
  categoriaId?: number | null;
  tamanho?: string;
  imagemIds?: number[];
};

const API_URL = "http://localhost:8080";

function imageUrl(imagemId?: number) {
  return imagemId ? `${API_URL}/imagens/${imagemId}` : "";
}

export const Route = createFileRoute("/produto/$id")({
  loader: async ({ params }) => {
    try {
      const p = await fetchProductFromApi(params.id);
      return { id: p.id };
    } catch {
      const p = getProduct(params.id);
      if (!p) throw notFound();
      return { id: p.id };
    }
  },

  head: ({ loaderData }) => {
    const p = loaderData ? getProduct(loaderData.id) : undefined;
    const t = p ? `${p.name} — Vênus` : "Peça não encontrada — Vênus";

    return {
      meta: [
        { title: t },
        { name: "description", content: p?.description ?? "" },
        { property: "og:title", content: t },
        { property: "og:description", content: p?.description ?? "" },
      ],
    };
  },

  notFoundComponent: () => (
    <div className="p-16 text-center">
      <p>Peça não encontrada.</p>
      <Link to="/loja" className="btn-primary mt-4">
        Voltar ao catálogo
      </Link>
    </div>
  ),

  component: ProductPage,
});

function ProductPage() {
  const { id } = Route.useParams();
  const nav = useNavigate();
  const { addToCart } = useStore();

  const [product, setProduct] = useState<Product>(() => {
    const found = getProduct(id) ?? PRODUCTS[0];

    if (!found) {
      throw new Error("Nenhum produto disponível.");
    }

    return found;
  });

  const [size, setSize] = useState<Size | null>(null);
  const [erro, setErro] = useState("");
  const [recs, setRecs] = useState<Product[]>([]);

  // Calcula o estoque total da peça.
  const totalStock = getProductTotalStock(product);

  useEffect(() => {
    let ativo = true;

    setSize(null);
    setErro("");
    setRecs([]);

    async function carregarProduto() {
      try {
        const p = await fetchProductFromApi(id);

        if (ativo) {
          setProduct(p);
        }
      } catch (e) {
        console.error("Erro ao carregar produto:", e);
      }
    }

    async function carregarSugestoes() {
      try {
        const resposta = (await recommendationApi.byProduct(id)) as ApiProduct[];

        const sugestoes = await Promise.all(
          resposta.map(async (p) => {
            let imagem = "";

            if (Array.isArray(p.imagemIds) && p.imagemIds.length > 0) {
              imagem = imageUrl(p.imagemIds[0]);
            }

            const base = getProduct(String(p.id)) ?? PRODUCTS[0];

            return {
              ...base,
              id: String(p.id),
              backendId: p.id,
              name: p.nome,
              description: p.descricao ?? "",
              priceCents: Math.round(Number(p.preco) * 100),
              condition: p.tipo === "SEMINOVO" ? "Seminovo" : "Novo",
              customizable: p.tipo === "SEMINOVO",

              // Estoque recebido do Back-end.
              stockTotal: p.estoque,

              stock: {
                PP: p.estoque,
                P: p.estoque,
                M: p.estoque,
                G: p.estoque,
                GG: p.estoque,
              },

              image: imagem,

              color: base?.color ?? "",

              tags: base?.tags ?? {
                style: "",
                fabric: "",
                pattern: "",
                tone: "",
              },

              category: base?.category ?? "Blusas",
            } as Product;
          })
        );

        if (ativo) {
          setRecs(sugestoes);
        }
      } catch (e) {
        console.error("Erro ao carregar sugestões:", e);

        if (ativo) {
          setRecs([]);
        }
      }
    }

    void carregarProduto();
    void carregarSugestoes();

    return () => {
      ativo = false;
    };
  }, [id]);

  // Adiciona a peça ao carrinho.
  const buy = async () => {
    if (totalStock === 0) {
      setErro("Essa peça está esgotada e não pode ser comprada.");
      return;
    }

    if (!size) {
      setErro("Escolha um tamanho.");
      return;
    }

    if (product.stock[size] === 0) {
      setErro("Esse tamanho não está disponível em estoque.");
      return;
    }

    try {
      await addToCart({
        productId: product.id,
        size,
        quantity: 1,
        customizations: [],
      });

      nav({ to: "/pagamento" });
    } catch (e) {
      setErro(
        e instanceof Error ? e.message : "Faça login para continuar."
      );
    }
  };

  return (
    <div className="min-h-screen pb-16">
      <AppHeader />
      <SideTabs />

      <main className="mx-auto grid max-w-6xl gap-8 px-4 py-6 sm:px-8 md:grid-cols-2">

        {/* Imagem do produto */}
        <div className="rounded-2xl border bg-cream p-6">
          <img
            src={product.image || undefined}
            alt={product.name}
            width={768}
            height={960}
            className="mx-auto aspect-[4/5] w-full max-w-sm object-cover"
          />
        </div>

        {/* Informações do produto */}
        <div className="space-y-6">

          <div className="panel bg-blush">
            <h2 className="text-lg font-extrabold">
              {product.name}
            </h2>

            <p className="mt-1 text-sm">
              <span className="font-bold text-primary">
                Valor:
              </span>{" "}
              {brl(product.priceCents)}
            </p>

            {/* Avisos de estoque */}
            {totalStock === 0 ? (
              <p className="mt-2 text-sm font-bold text-destructive">
                Produto esgotado
              </p>
            ) : totalStock === 1 ? (
              <p className="mt-2 text-sm font-bold text-destructive">
                Resta apenas 1 unidade!
              </p>
            ) : totalStock <= 5 ? (
              <p className="mt-2 text-sm font-bold text-destructive">
                Restam apenas {totalStock} unidades!
              </p>
            ) : null}

            <p className="mt-3 text-sm font-bold text-primary">
              Tamanho:
            </p>

            <div className="mt-2 flex flex-wrap gap-2">
              {ALL_SIZES.map((s) => {
                const off = totalStock === 0 || product.stock[s] === 0;

                return (
                  <button
                    key={s}
                    disabled={off}
                    onClick={() => {
                      setSize(s);
                      setErro("");
                    }}
                    title={off ? "Esgotado" : undefined}
                    className={`chip ${
                      size === s ? "chip-active" : ""
                    } ${
                      off ? "line-through opacity-40" : ""
                    }`}
                  >
                    {s}
                  </button>
                );
              })}
            </div>

            {erro && (
              <p className="mt-2 text-xs font-semibold text-destructive">
                {erro}
              </p>
            )}
          </div>

          {/* Descrição */}
          <div>
            <p className="text-sm font-bold text-primary">
              Descrição:
            </p>

            <p className="mt-1 text-sm leading-relaxed">
              {product.description}
            </p>

            <p className="mt-2 text-xs text-muted-foreground">
              {product.category} · {product.color} · {product.condition}
            </p>
          </div>

          {/* Sugestões de produtos */}
          <div>
            <p className="text-sm font-bold text-primary">
              Sugestões parecidas:
            </p>

            <p className="text-[11px] text-muted-foreground">
              Sugestões vindas da API de recomendação.
            </p>

            <div className="mt-3 grid grid-cols-3 gap-3">
              {recs.map((r) => (
                <ProductCard
                  key={r.id}
                  product={r}
                  compact
                />
              ))}
            </div>
          </div>

          {/* Botões de ação */}
          <div className="flex flex-wrap gap-3">

            {product.customizable && (
              <Link
                to="/personalizar/$id"
                params={{ id: product.id }}
                className="btn-wine"
              >
                Personalizar peça
              </Link>
            )}

            <button
              onClick={buy}
              disabled={totalStock === 0}
              className={`btn-primary ${
                totalStock === 0
                  ? "cursor-not-allowed opacity-50"
                  : ""
              }`}
            >
              {totalStock === 0 ? "Produto esgotado" : "Comprar agora"}
            </button>

          </div>
        </div>
      </main>
    </div>
  );
}
