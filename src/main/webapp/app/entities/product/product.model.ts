import { IProductCategory } from 'app/entities/product-category/product-category.model';

export interface IProduct {
  id?: number;
  productName?: string;
  productDescription?: string | null;
  productSize?: string;
  pricePerDay?: number;
  productImageContentType?: string | null;
  productImage?: string | null;
  productCategory?: IProductCategory | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public productName?: string,
    public productDescription?: string | null,
    public productSize?: string,
    public pricePerDay?: number,
    public productImageContentType?: string | null,
    public productImage?: string | null,
    public productCategory?: IProductCategory | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
