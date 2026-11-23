
import { Link } from "@tanstack/react-router";
import type { Product } from "@/data/products";
import { getProductTotalStock } from "@/data/products";
import { brl } from "@/lib/pricing";

export function ProductCard({
  product,
  compact,
}: {
  product: Product;
  compact?: boolean;
}) {
  const stock = getProductTotalStock(product);

  return (
    <Link
      to="/produto/$id"
      params={{ id: product.id }}
      className="group block text-center"
    >
      <div className="relative overflow-hidden rounded-xl bg-cream">
        <img
          src={product.image}
          alt={product.name}
          loading="lazy"
          width={768}
          height={960}
          className="aspect-[4/5] w-full object-cover transition-transform duration-300 group-hover:scale-105"
        />

        {stock === 0 && (
          <span className="absolute left-2 top-2 rounded-full bg-red-800 px-3 py-1 text-[10px] font-bold text-white">
            Produto esgotado
          </span>
        )}

        {stock > 0 && stock <= 5 && (
          <span className="absolute left-2 top-2 rounded-full bg-amber-100 px-3 py-1 text-[10px] font-bold text-amber-900">
            Restam apenas {stock} unidades!
          </span>
        )}
      </div>

      <p className={`mt-2 font-semibold ${compact ? "text-xs" : "text-sm"}`}>
        {product.name}
      </p>

      <p className="text-xs font-bold text-primary">
        {brl(product.priceCents)}
      </p>

      {!compact && (
        <p className="text-[11px] text-muted-foreground">
          {product.condition}
        </p>
      )}
    </Link>
  );
}
