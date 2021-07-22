import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'company',
        data: { pageTitle: 'rentalstoreApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'rentalstoreApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'rentalstoreApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-category',
        data: { pageTitle: 'rentalstoreApp.productCategory.home.title' },
        loadChildren: () => import('./product-category/product-category.module').then(m => m.ProductCategoryModule),
      },
      {
        path: 'rental-order',
        data: { pageTitle: 'rentalstoreApp.rentalOrder.home.title' },
        loadChildren: () => import('./rental-order/rental-order.module').then(m => m.RentalOrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'rentalstoreApp.orderItem.home.title' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'item-balance-by-customer',
        data: { pageTitle: 'rentalstoreApp.itemBalanceByCustomer.home.title' },
        loadChildren: () => import('./item-balance-by-customer/item-balance-by-customer.module').then(m => m.ItemBalanceByCustomerModule),
      },
      {
        path: 'bill',
        data: { pageTitle: 'rentalstoreApp.bill.home.title' },
        loadChildren: () => import('./bill/bill.module').then(m => m.BillModule),
      },
      {
        path: 'bill-line-item',
        data: { pageTitle: 'rentalstoreApp.billLineItem.home.title' },
        loadChildren: () => import('./bill-line-item/bill-line-item.module').then(m => m.BillLineItemModule),
      },
      {
        path: 'bill-line-item-to-order-item',
        data: { pageTitle: 'rentalstoreApp.billLineItemToOrderItem.home.title' },
        loadChildren: () =>
          import('./bill-line-item-to-order-item/bill-line-item-to-order-item.module').then(m => m.BillLineItemToOrderItemModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'rentalstoreApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
