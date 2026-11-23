export interface CustomizationOption {
  id: string;
  level: 1 | 2;
  name: string;
  description: string;
  extraCents: number;
  fields: { id: string; label: string; type: "choice" | "color" | "text"; options?: string[] }[];
}

export const COLORS = ["PRETO", "BRANCO", "VERMELHO", "AZUL", "VERDE", "AMARELO", "ROSA"];
export const TINGIMENTO_COLORS = ["PRETO", "BRANCO", "VERMELHO", "AZUL", "VERDE", "AMARELO", "ROSA", "LILAS", "MARROM", "CINZA", "BEGE"];
export const COLOR_SWATCH: Record<string, string> = {
  PRETO: "#171717", BRANCO: "#fffdf9", VERMELHO: "#b83b3b", AZUL: "#6688d8", VERDE: "#8dbb70", AMARELO: "#e9c94d", ROSA: "#df83ae",
  LILAS: "#9c7bc5", MARROM: "#8b5a45", CINZA: "#858585", BEGE: "#d9bd9f",
};

export const CUSTOMIZATIONS: CustomizationOption[] = [
  { id: "BORDADO_DESENHO", level: 1, name: "Bordado — desenho", description: "Escolha um desenho, cor, tamanho e local do bordado.", extraCents: 500,
    fields: [
      { id: "desenho", label: "Escolha o desenho", type: "choice", options: ["BORBOLETA", "RAIO", "FLOR", "ESTRELA", "CORACAO", "LUA", "SOL", "PATINHA", "SORRISO", "COROA", "OLHO"] },
      { id: "cor", label: "Cor do bordado", type: "color" },
      { id: "tamanhoBordado", label: "Escolha o tamanho", type: "choice", options: ["PEQUENO", "MEDIO", "GRANDE"] },
      { id: "localAplicacao", label: "Onde aplicar?", type: "choice", options: ["PEITO", "COSTAS", "MANGA"] },
    ] },
  { id: "BORDADO_ESCRITA", level: 1, name: "Bordado — escrita", description: "Escolha o texto, fonte, cor e tamanho do bordado.", extraCents: 500,
    fields: [
      { id: "texto", label: "Escreva até 50 letras", type: "text" },
      { id: "fonte", label: "Escolha a fonte", type: "choice", options: ["SERIF", "SCRIPT", "MODERNA"] },
      { id: "cor", label: "Cor do bordado", type: "color" },
      { id: "tamanhoBordado", label: "Escolha o tamanho", type: "choice", options: ["PEQUENO", "MEDIO", "GRANDE"] },
    ] },
  { id: "TINGIMENTO", level: 2, name: "Tingimento", description: "Altere a cor de toda a peça dentro das opções disponíveis.", extraCents: 1000,
    fields: [{ id: "corTingimento", label: "Selecione a cor do tecido", type: "color" }] },
  { id: "APLICACAO_RENDA", level: 2, name: "Aplicação de renda", description: "Escolha uma renda e suas características de aplicação.", extraCents: 1200,
    fields: [
      { id: "tipoRenda", label: "Escolha a renda", type: "choice", options: ["FLORAL", "BOLHINHA", "TULE_BORDADA", "GUIPIR"] },
      { id: "cor", label: "Cor da renda", type: "color" },
      { id: "larguraRenda", label: "Largura da renda", type: "choice", options: ["FINA", "MEDIA", "LARGA"] },
    ] },
  { id: "ALTERACAO_MANGA", level: 2, name: "Alteração de manga", description: "Altere o comprimento ou transforme a manga da peça.", extraCents: 1800,
    fields: [{ id: "tipoManga", label: "Escolha o estilo", type: "choice", options: ["CURTA", "TRES_QUARTOS", "LONGA", "SEM_MANGA"] }] },
];

export const getCustomizationOption = (id: string) => CUSTOMIZATIONS.find((c) => c.id === id);




const customizationImages = import.meta.glob<string>(
  "../assets/customizations/*.png",
  {
    eager: true,
    query: "?url",
    import: "default",
  }
);

const imageFiles: Record<string, string> = {
  BORBOLETA: "borboleta.png",
  RAIO: "raio.png",
  FLOR: "flor.png",
  ESTRELA: "estrela.png",
  CORACAO: "coracao.png",
  LUA: "lua.png",
  SOL: "sol.png",
  PATINHA: "patinha.png",
  SORRISO: "sorriso.png",
  COROA: "coroa.png",
  OLHO: "olho.png",

  FLORAL: "rendaFloral.png",
  BOLHINHA: "rendaBolinha.png",
  TULE_BORDADA: "rendaTuleBordada.png",
  GUIPIR: "rendaGuiper.png",

  CURTA: "mangaCurta.png",
  TRES_QUARTOS: "mangaTresQuartos.png",
  LONGA: "mangaLonga.png",
  SEM_MANGA: "semManga.png",
};

export function getCustomizationImage(
  option: string
): string | undefined {
  const filename = imageFiles[option];

  if (!filename) return undefined;

  return customizationImages[
    `../assets/customizations/${filename}`
  ];
}
