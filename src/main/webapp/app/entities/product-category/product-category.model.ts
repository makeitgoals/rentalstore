export interface IProductCategory {
  id?: number;
  productCategoryName?: string;
  productCategoryDescription?: string | null;
}

export class ProductCategory implements IProductCategory {
  constructor(public id?: number, public productCategoryName?: string, public productCategoryDescription?: string | null) {}
}

export function getProductCategoryIdentifier(productCategory: IProductCategory): number | undefined {
  return productCategory.id;
}
