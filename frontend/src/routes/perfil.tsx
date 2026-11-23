
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useRef, useState } from "react";
import { Camera, UserRound, LogOut } from "lucide-react";
import { AppHeader, SideTabs } from "@/components/venus/AppHeader";
import { useStore } from "@/lib/store";
import { usuarioApi } from "@/services/venusApi";

export const Route = createFileRoute("/perfil")({
  component: Perfil,
});

function Perfil() {
  const nav = useNavigate();
  const { user, setUser } = useStore();
  const fileRef = useRef<HTMLInputElement>(null);

  const [nome, setNome] = useState(user?.name ?? "");
  const [email, setEmail] = useState(user?.email ?? "");
  const [telefone, setTelefone] = useState(user?.telefone ?? "");
  const [foto, setFoto] = useState(user?.fotoPerfil ?? null);

  const [cep, setCep] = useState(user?.cep ?? "");
  const [rua, setRua] = useState(user?.rua ?? "");
  const [numero, setNumero] = useState(user?.numero ?? "");
  const [complemento, setComplemento] = useState(user?.complemento ?? "");
  const [bairro, setBairro] = useState(user?.bairro ?? "");
  const [cidade, setCidade] = useState(user?.cidade ?? "");
  const [estado, setEstado] = useState(user?.estado ?? "");

  const [erro, setErro] = useState("");
  const [mensagem, setMensagem] = useState("");
  const [salvando, setSalvando] = useState(false);

  if (!user) {
    return (
      <main className="p-10 text-center">
        <p>Faça login para acessar seu perfil.</p>
        <button onClick={() => nav({ to: "/login" })}>
          Ir para o login
        </button>
      </main>
    );
  }

  const usuarioAtual = user;

  function selecionarFoto(e: React.ChangeEvent<HTMLInputElement>) {
    const arquivo = e.target.files?.[0];

    if (!arquivo) return;

    if (!arquivo.type.startsWith("image/")) {
      setErro("Selecione um arquivo de imagem.");
      return;
    }

    if (arquivo.size > 2 * 1024 * 1024) {
      setErro("A imagem deve ter no máximo 2 MB.");
      return;
    }

    const leitor = new FileReader();

    leitor.onload = () => {
      setFoto(leitor.result as string);
      setErro("");
    };

    leitor.readAsDataURL(arquivo);
  }

  async function salvarPerfil(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    if (!nome.trim() || !email.trim() || !telefone.trim()) {
      setErro("Preencha nome, e-mail e telefone.");
      return;
    }

    if (
      !cep.trim() ||
      !rua.trim() ||
      !numero.trim() ||
      !bairro.trim() ||
      !cidade.trim() ||
      !estado.trim()
    ) {
      setErro("Preencha todos os campos obrigatórios do endereço.");
      return;
    }

    setSalvando(true);
    setErro("");
    setMensagem("");

    try {
      const atualizado = await usuarioApi.atualizar(usuarioAtual.idUsuario, {
        username: email.trim(),
        nome: nome.trim(),
        telefone: telefone.trim(),
        fotoPerfil: foto,
        cep: cep.trim(),
        rua: rua.trim(),
        numero: numero.trim(),
        complemento: complemento.trim(),
        bairro: bairro.trim(),
        cidade: cidade.trim(),
        estado: estado.trim(),
      });

      setUser({
        idUsuario: atualizado.idUsuario,
        name: atualizado.nome,
        email: atualizado.username,
        telefone: atualizado.telefone,
        fotoPerfil: atualizado.fotoPerfil,
        cep: atualizado.cep ?? "",
        rua: atualizado.rua ?? "",
        numero: atualizado.numero ?? "",
        complemento: atualizado.complemento ?? "",
        bairro: atualizado.bairro ?? "",
        cidade: atualizado.cidade ?? "",
        estado: atualizado.estado ?? "",
      });

      setMensagem("Perfil e endereço atualizados com sucesso!");
    } catch (e) {
      setErro(
        e instanceof Error
          ? e.message
          : "Não foi possível atualizar seu perfil."
      );
    } finally {
      setSalvando(false);
    }
  }

  function sairDoPerfil() {
    setUser(null);
    nav({ to: "/login" });
  }

  return (
    <div className="min-h-screen pb-16">
      <AppHeader title="Meu Perfil" />
      <SideTabs />

      <main className="mx-auto max-w-2xl px-4 py-8 sm:px-8">
        <section className="panel bg-card">
          <div className="mb-6 text-center">
            <h2 className="text-xl font-extrabold text-primary">
              Minha conta
            </h2>
            <p className="mt-1 text-xs text-muted-foreground">
              Gerencie suas informações pessoais e endereço de entrega.
            </p>
          </div>

          <form onSubmit={salvarPerfil} className="space-y-5">
            <div className="flex flex-col items-center">
              <div className="relative">
                <div className="grid h-28 w-28 place-items-center overflow-hidden rounded-full border-2 border-primary bg-blush">
                  {foto ? (
                    <img
                      src={foto}
                      alt="Foto de perfil"
                      className="h-full w-full object-cover"
                    />
                  ) : (
                    <UserRound className="h-12 w-12 text-primary" />
                  )}
                </div>

                <button
                  type="button"
                  onClick={() => fileRef.current?.click()}
                  className="absolute bottom-0 right-0 grid h-9 w-9 place-items-center rounded-full bg-primary text-primary-foreground"
                  aria-label="Adicionar foto de perfil"
                >
                  <Camera className="h-4 w-4" />
                </button>
              </div>

              <input
                ref={fileRef}
                type="file"
                accept="image/*"
                className="hidden"
                onChange={selecionarFoto}
              />

              <button
                type="button"
                onClick={() => fileRef.current?.click()}
                className="mt-3 text-xs font-bold text-primary underline"
              >
                Adicionar ou alterar foto
              </button>
            </div>

            <div className="border-b pb-3">
              <h3 className="text-base font-bold text-primary">
                Dados pessoais
              </h3>
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                Nome completo
              </label>
              <input
                className="field"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                placeholder="Digite seu nome"
                required
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                E-mail
              </label>
              <input
                className="field"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Digite seu e-mail"
                required
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                Telefone
              </label>
              <input
                className="field"
                type="tel"
                value={telefone}
                onChange={(e) => setTelefone(e.target.value)}
                placeholder="Digite seu telefone"
                required
              />
            </div>

            <div className="border-b pb-3 pt-4">
              <h3 className="text-base font-bold text-primary">
                Endereço de entrega
              </h3>
              <p className="mt-1 text-xs text-muted-foreground">
                Preencha seu endereço para conseguir finalizar suas compras.
                Os campos marcados com * são obrigatórios.
              </p>
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                CEP *
              </label>
              <input
                className="field"
                value={cep}
                onChange={(e) => setCep(e.target.value)}
                placeholder="00000-000"
                maxLength={9}
                required
              />
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                Rua *
              </label>
              <input
                className="field"
                value={rua}
                onChange={(e) => setRua(e.target.value)}
                placeholder="Nome da rua ou avenida"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="mb-1 block text-sm font-semibold">
                  Número *
                </label>
                <input
                  className="field"
                  value={numero}
                  onChange={(e) => setNumero(e.target.value)}
                  placeholder="Número"
                  required
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-semibold">
                  Complemento
                </label>
                <input
                  className="field"
                  value={complemento}
                  onChange={(e) => setComplemento(e.target.value)}
                  placeholder="Apto, bloco..."
                />
              </div>
            </div>

            <div>
              <label className="mb-1 block text-sm font-semibold">
                Bairro *
              </label>
              <input
                className="field"
                value={bairro}
                onChange={(e) => setBairro(e.target.value)}
                placeholder="Digite seu bairro"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="mb-1 block text-sm font-semibold">
                  Cidade *
                </label>
                <input
                  className="field"
                  value={cidade}
                  onChange={(e) => setCidade(e.target.value)}
                  placeholder="Sua cidade"
                  required
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-semibold">
                  Estado *
                </label>
                <input
                  className="field"
                  value={estado}
                  onChange={(e) => setEstado(e.target.value)}
                  placeholder="UF"
                  maxLength={2}
                  required
                />
              </div>
            </div>

            {erro && (
              <p className="text-sm font-semibold text-destructive">
                {erro}
              </p>
            )}

            {mensagem && (
              <p className="text-sm font-semibold text-green-700">
                {mensagem}
              </p>
            )}

            <button
              type="submit"
              disabled={salvando}
              className="btn-wine w-full disabled:opacity-50"
            >
              {salvando ? "SALVANDO..." : "SALVAR ALTERAÇÕES"}
            </button>
          </form>
        </section>

        <button
          onClick={sairDoPerfil}
          className="mt-6 flex w-full items-center justify-center gap-2 rounded-lg border border-destructive/30 py-3 text-sm font-bold text-destructive hover:bg-destructive/5"
        >
          <LogOut className="h-4 w-4" />
          Sair do perfil
        </button>
      </main>
    </div>
  );
}
