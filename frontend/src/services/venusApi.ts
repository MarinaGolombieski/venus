
import { api, apiBaseUrl } from "./api";

export type BackendProduct = {
  id: number;
  nome: string;
  descricao: string;
  preco: number;
  estoque: number;
  tipo: "NOVO" | "SEMINOVO";
  estado?: "COMO_NOVO" | "POUCO_USADO" | "USADO" | null;
  categoriaId?: number | null;
  tamanho?: "PP" | "P" | "M" | "G" | "GG" | null;
  imagemIds?: number[];
};

export type BackendUser = {
  idUsuario: number;
  username: string;
  role: string;
  nome: string;
  telefone: string;
  fotoPerfil: string | null;

  // Endereço do usuário
  cep: string | null;
  rua: string | null;
  numero: string | null;
  complemento: string | null;
  bairro: string | null;
  cidade: string | null;
  estado: string | null;
};

export type BackendCartItem = {
  idItemCarrinho: number;
  quantidade: number;
  carrinhoId: number;
  produtoId: number;
  produtoNome: string;
  tamanho?: "PP" | "P" | "M" | "G" | "GG";
  precoProduto: number;
  valorPersonalizacoes: number;
  precoUnitarioFinal: number;
  subtotal: number;
  personalizacoes: BackendCustomization[];
};

export type BackendCart = {
  idCarrinho: number;
  usuarioId: number;
  itens: BackendCartItem[];
  valorTotal: number;
};

export type BackendCustomization = {
  idPersonalizacao: number;
  nivel: "DETALHE" | "TRANSFORMACAO";
  tipo: string;
  valor: number;
  itemCarrinhoId?: number;
  itemPedidoId?: number;
  desenho?: string;
  cor?: string;
  tamanhoBordado?: string;
  localAplicacao?: string;
  texto?: string;
  fonte?: string;
  tipoRenda?: string;
  larguraRenda?: string;
  corTingimento?: string;
  tipoManga?: string;
};

export type PersonalizationPayload = {
  itemCarrinhoId: number;
  tipo: string;
  desenho?: string;
  cor?: string;
  tamanhoBordado?: string;
  localAplicacao?: string;
  texto?: string;
  fonte?: string;
  tipoRenda?: string;
  larguraRenda?: string;
  corTingimento?: string;
  tipoManga?: string;
};

export const productImageUrl = (id: number) =>
  `${apiBaseUrl}/imagens/${id}`;

export const productsApi = {
  list: () => api<BackendProduct[]>("/produtos"),
  get: (id: string | number) => api<BackendProduct>(`/produtos/${id}`),
};

export const optionsApi = {
  all: () => api<Record<string, unknown>>("/opcoes/personalizacoes"),
};

export const authApi = {
  register: (payload: {
    username: string;
    senha: string;
    nome: string;
    telefone: string;
    role?: string;
  }) =>
    api<BackendUser>("/usuarios", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  login: (payload: { username: string; senha: string }) =>
    api<BackendUser>("/auth/login", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
};

export const usuarioApi = {
  atualizar: (
    id: number,
    dados: {
      username: string;
      nome: string;
      telefone: string;
      fotoPerfil: string | null;

      // Endereço do usuário
      cep: string;
      rua: string;
      numero: string;
      complemento: string;
      bairro: string;
      cidade: string;
      estado: string;
    }
  ) =>
    api<BackendUser>(`/usuarios/${id}`, {
      method: "PUT",
      body: JSON.stringify(dados),
    }),
};

export const cartApi = {
  getByUser: (userId: number) =>
    api<BackendCart>(`/carrinhos/usuario/${userId}`),

  create: (userId: number) =>
    api<BackendCart>("/carrinhos", {
      method: "POST",
      body: JSON.stringify({ usuarioId: userId }),
    }),

  addItem: async (
    cartId: number,
    produtoId: number,
    quantidade = 1,
    tamanho?: string
  ): Promise<BackendCartItem> => {
    const result = await api<BackendCartItem>(
      `/carrinhos/${cartId}/itens`,
      {
        method: "POST",
        body: JSON.stringify({ produtoId, quantidade, tamanho }),
      }
    );

    console.log("Resposta ao adicionar item:", result);

    if (
      !result ||
      typeof result.idItemCarrinho !== "number"
    ) {
      throw new Error(
        "A API não retornou um idItemCarrinho válido. Confira o console."
      );
    }

    return result;
  },

  updateItem: (itemId: number, quantidade: number) =>
    api<BackendCartItem>(
      `/carrinhos/itens/${itemId}?quantidade=${quantidade}`,
      { method: "PUT" }
    ),

  removeItem: (itemId: number) =>
    api<void>(`/carrinhos/itens/${itemId}`, {
      method: "DELETE",
    }),
};

export const customizationApi = {
  add: (payload: PersonalizationPayload) =>
    api<BackendCustomization>("/personalizacoes", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  remove: (id: number) =>
    api<void>(`/personalizacoes/${id}`, {
      method: "DELETE",
    }),
};

export const orderApi = {
  checkout: (carrinhoId: number) =>
    api<{
      idPedido: number;
      valorTotal: number;
      status: string;
      pagamentoId?: number;
    }>("/pedidos/checkout", {
      method: "POST",
      body: JSON.stringify({ carrinhoId: carrinhoId }),
    }),

  get: (id: number) => api<any>(`/pedidos/${id}`),
  list: () => api<any[]>("/pedidos"),
};

export const paymentApi = {
  create: (
    pedidoId: number,
    metodoPagamento: "PIX" | "CARTAO" | "BOLETO"
  ) =>
    api<any>("/pagamentos", {
      method: "POST",
      body: JSON.stringify({ pedidoId, metodoPagamento }),
    }),
};

export const recommendationApi = {
  byProduct: (id: string | number) =>
    api<BackendProduct[]>(`/recomendacoes/produto/${id}`),
};

export const aiApi = {
  preview: (payload: {
    produtoId: number;
    tipoAlteracao: string;
    detalhes: Record<string, string>;
  }) =>
    api<{
      status: string;
      message: string;
      imageUrl: string | null;
    }>("/ia/preview", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
};
