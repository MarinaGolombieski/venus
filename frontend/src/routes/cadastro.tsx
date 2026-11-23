import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { LogoMark } from "@/components/venus/Logo";
import { mapBackendUser, useStore } from "@/lib/store";
import { authApi } from "@/services/venusApi";

export const Route = createFileRoute("/cadastro")({
  head: () => ({ meta: [
    { title: "Criar conta — Vênus" }, { name: "description", content: "Cadastre-se na Vênus." },
    { property: "og:title", content: "Criar conta — Vênus" }, { property: "og:description", content: "Cadastre-se na Vênus." },
  ] }),
  component: Cadastro,
});

const FIELDS = [
  { id: "nome", label: "Nome completo" }, { id: "email", label: "E-mail", type: "email" },
  { id: "cpf", label: "CPF" }, { id: "telefone", label: "Telefone" },
  { id: "nascimento", label: "Data de nascimento", type: "date" }, { id: "senha", label: "Senha (mín. 8 caracteres)", type: "password" },
];

function validCPF(c: string) {
  const d = c.replace(/\D/g, ""); 
  if (d.length !== 11 || /^(\d)\1+$/.test(d)) 
    return false;
  const calc = (n: number) => { let s = 0; for (let i = 0; i < n; i++) s += Number(d[i]) * (n + 1 - i); const r = (s * 10) % 11; return r === 10 ? 0 : r; };
  return calc(9) === Number(d[9]) && calc(10) === Number(d[10]);
}

function Cadastro() {
  const nav = useNavigate(); const { setUser } = useStore();
  const [v, setV] = useState<Record<string, string>>({}); const [termos, setTermos] = useState(false); const [erro, setErro] = useState("");
  return (
    <main className="flex min-h-screen items-center justify-center bg-cream px-6 py-10">
      <form className="w-full max-w-md text-center" onSubmit={async (e) => {
        e.preventDefault();
        const g = (k: string) => v[k] ?? "";
        if (FIELDS.some((f) => !v[f.id]?.trim())) return setErro("Preencha todos os campos obrigatórios.");
        if (!/^\S+@\S+\.\S+$/.test(g("email"))) return setErro("E-mail inválido.");
        if (!validCPF(g("cpf"))) return setErro("CPF inválido.");
        if (g("telefone").replace(/\D/g, "").length < 10) return setErro("Telefone inválido.");
        if (g("senha").length < 8) return setErro("A senha precisa ter pelo menos 8 caracteres.");
        if (!termos) return setErro("Aceite os termos de uso para continuar.");
        try {
const u = await authApi.register({
  username: g("email"),
  senha: g("senha"),
  nome: g("nome"),
  telefone: g("telefone")
});          setUser(mapBackendUser(u));
          nav({ to: "/loja" });
        } catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível criar a conta."); }
      }}>
        <LogoMark className="mx-auto h-14 w-14 text-primary" />
        <h1 className="font-display mt-2 text-2xl font-bold text-primary">WalkWorld</h1>
        <p className="mx-auto mt-2 max-w-xs text-xs text-muted-foreground">Plataforma para quem deseja peças exclusivas, sustentáveis e feitas para você.</p>
        <div className="panel mt-6 space-y-3 text-left">
          <p className="text-center text-sm font-bold text-primary">Dados pessoais</p>
          {FIELDS.map((f) => (
            <input key={f.id} aria-label={f.label} placeholder={f.label} type={f.type ?? "text"} className="field bg-card"
              value={v[f.id] ?? ""} onChange={(e) => setV({ ...v, [f.id]: e.target.value })} />
          ))}
          <label className="flex items-center gap-2 text-xs"><input type="checkbox" checked={termos} onChange={(e) => setTermos(e.target.checked)} /> Li e aceito os termos de uso</label>
        </div>
        {erro && <p className="mt-3 text-xs font-semibold text-destructive">{erro}</p>}
        <button className="btn-wine mt-6 w-40">ENTRAR</button>
        <p className="mt-3 text-xs">Já tem conta? <Link to="/login" className="font-bold text-primary underline">Entrar</Link></p>
      </form>
    </main>
  );
}
