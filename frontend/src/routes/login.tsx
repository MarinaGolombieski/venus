import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { Lock, Mail } from "lucide-react";
import { LogoMark } from "@/components/venus/Logo";
import { mapBackendUser, useStore } from "@/lib/store";
import { authApi } from "@/services/venusApi";
import { useEffect } from "react";

export const Route = createFileRoute("/login")({
  head: () => ({ meta: [
    { title: "Entrar — Vênus" }, { name: "description", content: "Acesse sua conta Vênus." },
    { property: "og:title", content: "Entrar — Vênus" }, { property: "og:description", content: "Acesse sua conta Vênus." },
  ] }),
  component: Login,
});

function Login() {
  const nav = useNavigate();
  const { setUser } = useStore();
  const [email, setEmail] = useState(""); const [senha, setSenha] = useState(""); const [erro, setErro] = useState("");

  const [mounted, setMounted] = useState(false);

useEffect(() => {
  setMounted(true);
}, []);

  return (
    <main className="flex min-h-screen items-center justify-center bg-cream px-6">
      <form className="w-full max-w-sm text-center" onSubmit={async (e) => {
        e.preventDefault();
        if (!/^\S+@\S+\.\S+$/.test(email)) return setErro("Digite um e-mail válido.");
        if (senha.length < 8) return setErro("A senha precisa ter pelo menos 8 caracteres.");
        try {
          const u = await authApi.login({ username: email, senha });
          setUser(mapBackendUser(u));
          nav({ to: "/loja" });
        } catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível entrar."); }
      }}>
        <LogoMark className="mx-auto h-14 w-14 text-primary" />
        <h1 className="font-display mt-2 text-2xl font-bold text-primary">WalkWorld</h1>
        <p className="mt-4 font-bold text-primary">Bem-vinda(o) de volta!</p>
        <p className="text-xs text-muted-foreground">Faça seu login para continuar</p>
        <div className="mt-6 space-y-3 text-left">
          <label className="relative block"><Mail className="absolute left-3 top-3 h-4 w-4 text-primary" />
            <input className="field pl-9" placeholder="Digite seu e-mail" type="email" value={email} onChange={(e) => setEmail(e.target.value)} /></label>
          <label className="relative block"><Lock className="absolute left-3 top-3 h-4 w-4 text-primary" />
            <input className="field pl-9" placeholder="Digite sua senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)} /></label>
        </div>
        {erro && <p className="mt-3 text-xs font-semibold text-destructive">{erro}</p>}
        <button className="btn-wine mt-6 w-40">ENTRAR</button>
        <p className="mt-3 text-xs">Não tem cadastro? <Link to="/cadastro" className="font-bold text-primary underline">Faça já</Link></p>
      </form>
    </main>
  );
}
