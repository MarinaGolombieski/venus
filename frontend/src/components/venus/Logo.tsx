import logoVenus from "@/assets/venus-logo.png";

export function LogoMark({ className = "h-6 w-6" }: { className?: string }) {
  return (
    <img
      src={logoVenus}
      className={`${className} object-contain`}
      alt="Vênus"
      aria-hidden={false}
    />
  );
}

export function Logo() {
  return (
    <span className="inline-flex items-center gap-1.5 text-primary">
      <LogoMark className="h-8 w-8" />
      <span className="font-display text-base font-bold">Vênus</span>
    </span>
  );
}
