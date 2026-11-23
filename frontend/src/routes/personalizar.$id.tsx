
import {
  createFileRoute,
  Link,
  notFound,
  useNavigate,
} from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { ChevronRight, Plus } from "lucide-react";

import { AppHeader, SideTabs } from "@/components/venus/AppHeader";

import {
  ALL_SIZES,
  fetchProductFromApi,
  getProduct,
  PRODUCTS,
  type Product,
  type Size,
} from "@/data/products";

import {
  COLORS,
  COLOR_SWATCH,
  TINGIMENTO_COLORS,
  CUSTOMIZATIONS,
  getCustomizationImage,
  type CustomizationOption,
} from "@/data/customizations";

import { brl } from "@/lib/pricing";
import { useStore } from "@/lib/store";


export const Route = createFileRoute("/personalizar/$id")({
  loader: async ({ params }) => {
    try {
      const product = await fetchProductFromApi(params.id);

      if (!product.customizable) {
        throw notFound();
      }

      return { id: product.id };
    } catch (error) {
      // Preserva o tratamento de 404 do TanStack Router.
      if (
        error &&
        typeof error === "object" &&
        "isNotFound" in error
      ) {
        throw error;
      }

      const product = getProduct(params.id);

      if (!product || !product.customizable) {
        throw notFound();
      }

      return { id: product.id };
    }
  },

  head: () => ({
    meta: [
      {
        title: "Personalizar peça — Vênus",
      },
      {
        name: "description",
        content: "Bordado, tingimento, renda e alteração de manga.",
      },
    ],
  }),

  notFoundComponent: () => (
    <div className="p-16 text-center">
      Peça não disponível para personalização.{" "}
      <Link
        to="/personalizar"
        className="text-primary underline"
      >
        Ver peças
      </Link>
    </div>
  ),

  component: Personalizar,
});

function Personalizar() {
  const { id } = Route.useParams();

  const [product, setProduct] = useState<Product>(() => {
    const found = getProduct(id) ?? PRODUCTS[0];

    if (!found) {
      throw new Error("Nenhum produto disponível para exibição.");
    }

    return found;
  });

  const nav = useNavigate();
  const { addToCart, addCustomization } = useStore();

  const [level, setLevel] = useState<1 | 2 | null>(null);
  const [opt, setOpt] = useState<CustomizationOption | null>(null);
  const [details, setDetails] = useState<Record<string, string>>({});
  const [size, setSize] = useState<Size | null>(null);
  const [erro, setErro] = useState("");
  const [aiMessage, setAiMessage] = useState("");
  const [aiImage, setAiImage] = useState<string | null>(null);

  useEffect(() => {
    let active = true;

    setLevel(null);
    setOpt(null);
    setDetails({});
    setSize(null);
    setErro("");
    setAiMessage("");
    setAiImage(null);

    void fetchProductFromApi(id)
      .then((loadedProduct) => {
        if (active) {
          setProduct(loadedProduct);
        }
      })
      .catch((error) => {
        console.error(
          "Não foi possível carregar o produto para personalização:",
          error
        );
      });

    return () => {
      active = false;
    };
  }, [id]);

  const price =
    (product?.priceCents ?? 0) + (opt?.extraCents ?? 0);

  

  

  const confirm = async () => {
    setErro("");

    if (!opt) {
      setErro("Escolha uma personalização.");
      return;
    }

    const missing = opt.fields.find(
      (field) => !details[field.id]?.trim()
    );

    if (missing) {
      setErro(`Preencha: ${missing.label}.`);
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
      const created = await addToCart({
        productId: id,
        size,
        quantity: 1,
        customizations: [],
      });

      if (!created.backendItemId) {
        throw new Error(
          "Não foi possível identificar o item do carrinho."
        );
      }

      await addCustomization(created.backendItemId, {
        tipo: opt.id,
        ...details,
      });

      await nav({ to: "/pagamento" });
    } catch (error) {
      setErro(
        error instanceof Error
          ? error.message
          : "Não foi possível salvar a personalização."
      );
    }
  };

  return (
    <div className="min-h-screen pb-16">
      <AppHeader
        title={
          opt
            ? `Nível ${opt.level} · ${
                opt.level === 1 ? "Detalhes" : "Transformação"
              }`
            : "Personalize sua peça"
        }
        subtitle={
          opt?.name ?? "Escolha o nível de personalização"
        }
      />

      <SideTabs />

      <main className="mx-auto grid max-w-6xl gap-8 px-4 py-6 sm:px-8 md:grid-cols-[1fr_1.4fr]">
        <aside className="panel bg-blush text-center">
          <img
            src={product.image || undefined}
            alt={product.name}
            width={768}
            height={960}
            className="mx-auto aspect-[4/5] w-full max-w-[220px] rounded-lg object-cover"
          />

          <p className="mt-2 text-sm font-bold">
            {product.name}
          </p>

          <p
            className="text-sm font-extrabold text-primary"
            aria-live="polite"
          >
            {brl(price)}
          </p>

          {opt && (
            <p className="text-[11px] text-muted-foreground">
              inclui {opt.name}: +{brl(opt.extraCents)}
            </p>
          )}
        </aside>

        <section className="space-y-4">
          {!opt && (
            <>
              <p className="text-xs text-muted-foreground">
                Escolha o nível de personalização:
              </p>

              {[1, 2].map((currentLevel) => (
                <div key={currentLevel}>
                  <button
                    type="button"
                    onClick={() =>
                      setLevel(
                        level === currentLevel
                          ? null
                          : (currentLevel as 1 | 2)
                      )
                    }
                    className="panel flex w-full items-start justify-between text-left"
                  >
                    <span>
                      <span className="text-xs font-extrabold text-primary">
                        NÍVEL {currentLevel} –{" "}
                        {currentLevel === 1
                          ? "DETALHES"
                          : "TRANSFORMAÇÃO"}
                      </span>

                      <span className="block text-xs">
                        {currentLevel === 1
                          ? "Desenhos e escritas aplicados à peça."
                          : "Tingimento, renda ou alteração de manga."}
                      </span>
                    </span>

                    <Plus className="h-4 w-4 text-primary" />
                  </button>

                  {level === currentLevel && (
                    <div className="mt-3 grid gap-3 sm:grid-cols-2">
                      {CUSTOMIZATIONS.filter(
                        (customization) =>
                          customization.level === currentLevel
                      ).map((customization) => (
                        <button
                          type="button"
                          key={customization.id}
                          onClick={() => {
                            setOpt(customization);
                            setDetails({});
                            setErro("");
                            setAiMessage("");
                            setAiImage(null);
                          }}
                          className="panel text-left transition hover:ring-2 hover:ring-primary"
                        >
                          <span className="flex justify-between text-sm font-bold text-primary">
                            {customization.name}
                            <ChevronRight className="h-4 w-4" />
                          </span>

                          <span className="mt-1 block text-xs">
                            {customization.description}
                          </span>

                          <span className="mt-2 block text-xs font-bold">
                            +{brl(customization.extraCents)}
                          </span>
                        </button>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </>
          )}

          {opt && (
            <div className="space-y-5">
              <button
                type="button"
                onClick={() => {
                  setOpt(null);
                  setErro("");
                  setAiMessage("");
                  setAiImage(null);
                }}
                className="text-xs font-semibold text-primary underline"
              >
                ← Trocar personalização
              </button>

              {opt.fields.map((field) => (
                <div key={field.id}>
                  <p className="mb-2 text-sm font-bold text-primary">
                    {field.label}
                  </p>

                  {field.type === "text" && (
                    <input
                      className="field"
                      maxLength={50}
                      placeholder="Digite aqui"
                      value={details[field.id] ?? ""}
                      onChange={(event) =>
                        setDetails((previous) => ({
                          ...previous,
                          [field.id]: event.target.value,
                        }))
                      }
                    />
                  )}

                 
{field.type === "choice" && (
  <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
    {field.options!.map((option) => {
      const image = getCustomizationImage(option);
      const selected = details[field.id] === option;

      const isFont = field.id === "fonte";

      return (
        <button
          type="button"
          key={option}
          onClick={() =>
            setDetails((previous) => ({
              ...previous,
              [field.id]: option,
            }))
          }
          className={`overflow-hidden rounded-xl border text-left transition ${
            selected
              ? "border-primary ring-2 ring-primary"
              : "border-border hover:border-primary/50"
          }`}
        >
          {isFont ? (
            <div className="flex h-24 items-center justify-center bg-cream p-3">
              <span
                className={`text-2xl ${
                  option === "SERIF"
                    ? "font-serif"
                    : option === "SCRIPT"
                      ? "italic"
                      : "font-sans font-semibold"
                }`}
              >
              {(details["texto"] ?? "").trim() || "Vênus"}              </span>
            </div>
          ) : image ? (
            <img
              src={image}
              alt={option.replaceAll("_", " ")}
              className="h-24 w-full bg-white object-contain p-2"
              loading="lazy"
            />
          ) : null}

          <span className="block px-2 py-3 text-center text-xs font-semibold">
            {option.replaceAll("_", " ")}
          </span>
        </button>
      );
    })}
  </div>
)}


                  {field.type === "color" && (
                    <div className="flex flex-wrap gap-2">
                      {(opt.id === "TINGIMENTO"
                        ? TINGIMENTO_COLORS
                        : COLORS
                      ).map((color) => (
                        <button
                          type="button"
                          key={color}
                          aria-label={color}
                          title={color}
                          onClick={() =>
                            setDetails((previous) => ({
                              ...previous,
                              [field.id]: color,
                            }))
                          }
                          style={{
                            background: COLOR_SWATCH[color],
                          }}
                          className={`h-8 w-8 rounded-full border ${
                            details[field.id] === color
                              ? "ring-2 ring-primary ring-offset-2"
                              : ""
                          }`}
                        />
                      ))}
                    </div>
                  )}
                </div>
              ))}

              <div>
                <p className="mb-2 text-sm font-bold text-primary">
                  Tamanho
                </p>

                <div className="flex gap-2">
                  {ALL_SIZES.map((currentSize) => (
                    <button
                      type="button"
                      key={currentSize}
                      disabled={product.stock[currentSize] === 0}
                      onClick={() => setSize(currentSize)}
                      className={`chip ${
                        size === currentSize ? "chip-active" : ""
                      } disabled:line-through disabled:opacity-40`}
                    >
                      {currentSize}
                    </button>
                  ))}
                </div>
              </div>

              <div className="rounded-xl border border-primary/20 bg-cream p-4">
                <div className="flex items-center gap-2 text-sm font-bold text-primary">
                  Prévia com IA
                </div>

                <p className="mt-1 text-[11px] text-muted-foreground">
                  A IA poderá mostrar uma simulação da peça depois
                  da alteração escolhida. A chave do provedor fica
                  no Back-end.
                </p>

                <button
                  type="button"
                  className="btn-wine mt-3"
                >
                  Gerar prévia
                </button>

                {aiMessage && (
                  <p className="mt-2 text-xs">{aiMessage}</p>
                )}

                {aiImage && (
                  <img
                    src={aiImage}
                    alt="Prévia da personalização gerada por IA"
                    className="mt-3 max-h-72 w-full rounded-lg object-contain"
                  />
                )}
              </div>

              {erro && (
                <p className="text-xs font-semibold text-destructive">
                  {erro}
                </p>
              )}

              <button
                type="button"
                onClick={() => void confirm()}
                className="btn-primary"
              >
                Adicionar ao carrinho · {brl(price)}
              </button>
            </div>
          )}
        </section>
      </main>
    </div>
  );
}
