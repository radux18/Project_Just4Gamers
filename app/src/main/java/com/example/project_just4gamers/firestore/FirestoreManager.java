package com.example.project_just4gamers.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project_just4gamers.ui.activities.PodiumActivity;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.ui.activities.ActivateCouponActivity;
import com.example.project_just4gamers.ui.activities.AddAddressActivity;
import com.example.project_just4gamers.ui.activities.AddMessageActivity;
import com.example.project_just4gamers.ui.activities.AddProductActivity;
import com.example.project_just4gamers.ui.activities.AddReviewActivity;
import com.example.project_just4gamers.ui.activities.AddressListActivity;
import com.example.project_just4gamers.ui.activities.CartListActivity;
import com.example.project_just4gamers.ui.activities.CheckoutActivity;
import com.example.project_just4gamers.ui.activities.DashboardActivity;
import com.example.project_just4gamers.ui.activities.DiscountCouponsActivity;
import com.example.project_just4gamers.ui.activities.FavoritesActivity;
import com.example.project_just4gamers.ui.activities.LoginActivity;
import com.example.project_just4gamers.ui.activities.MessageViewActivity;
import com.example.project_just4gamers.ui.activities.ProductDetailsActivity;
import com.example.project_just4gamers.ui.activities.ProductOwnerProfileActivity;
import com.example.project_just4gamers.ui.activities.RegisterActivity;
import com.example.project_just4gamers.ui.activities.SettingsActivity;
import com.example.project_just4gamers.ui.activities.UpdateProductActivity;
import com.example.project_just4gamers.ui.activities.UserProfileActivity;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.ui.fragments.DashboardFragment;
import com.example.project_just4gamers.ui.fragments.OrdersFragment;
import com.example.project_just4gamers.ui.fragments.ProductsFragment;
import com.example.project_just4gamers.ui.fragments.ReceivedMessagesFragment;
import com.example.project_just4gamers.ui.fragments.SentMessagesFragment;
import com.example.project_just4gamers.ui.fragments.SoldProductsFragment;
import com.example.project_just4gamers.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FirestoreManager {
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public void registerUser(RegisterActivity activity, User userInfo){
        fStore.collection(Constants.getUSERS())
                .document(userInfo.getId())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.hideProgressDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.hideProgressDialog();
                    }
                });
    }

    public String getCurrentUserID(){
        // An instance of currentUser using FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // A variable to assign the currentUserId if it is not null or else it will be blank.
        String currentUserID = "";
        if (currentUser != null){
            currentUserID = currentUser.getUid();
        }
        return currentUserID;
    }

    public void getUserDetails(Activity activity){
        //Here we pass the collection name from which we wants the data.
        fStore.collection(Constants.getUSERS())
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        //Here we have received the document snapshot which is converted into the User Data model object.
                        User user = document.toObject(User.class);

                        SharedPreferences prefs = activity.getSharedPreferences(Constants.getPREFS(),
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(Constants.getUSERNAME(), user.getFirstName() + " " + user.getLastName());
                        editor.apply();

                        if (activity instanceof LoginActivity){
                            ((LoginActivity) activity).hideProgressDialog();

                            if (user.getProfileCompleted() == 0){
                                Intent intent = new Intent(activity.getApplicationContext(), UserProfileActivity.class);
                                intent.putExtra(Constants.getExtraUserDetails(),user);
                                activity.startActivity(intent);
                            } else {
                                Intent intent = new Intent(activity.getApplicationContext(), DashboardActivity.class);
                                activity.startActivity(intent);
                            }
                            activity.finish();

                        } else if (activity instanceof SettingsActivity){
                            ((SettingsActivity) activity).userDetailsSuccess(user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (activity instanceof LoginActivity){
                    ((LoginActivity) activity).hideProgressDialog();
                } else if (activity instanceof SettingsActivity){
                   ((SettingsActivity) activity).hideProgressDialog();
                }
            }
        });
    }

    public void updateUserProfileData(Activity activity, HashMap<String, Object> userHashMap){
        fStore.collection(Constants.getUSERS())
                    .document(getCurrentUserID())
                    .update(userHashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //hide progress bar
                            if (activity instanceof UserProfileActivity){
                                ((UserProfileActivity) activity).userProfileUpdateSuccess();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress bar
            }
        });

    }

    public void uploadImageToCloudStorage(Activity activity, Uri imageFileUri, String imageType){
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(
                imageType + System.currentTimeMillis() + "."
                + new Constants().getFileExtension(
                        activity,
                        imageFileUri
                )
        );
        reference.putFile(imageFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Get the downloadable url from the task snapshot
                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (activity instanceof UserProfileActivity){
                                    ((UserProfileActivity) activity).imageUploadSuccess(uri.toString());
                                } else if (activity instanceof AddProductActivity){
                                    ((AddProductActivity) activity).imageUploadSuccess(uri.toString());
                                } else if (activity instanceof UpdateProductActivity){
                                    ((UpdateProductActivity) activity).imageUploadSuccess(uri.toString());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (activity instanceof UserProfileActivity){
                            //hide the progress bar
                            Toast.makeText(activity.getApplicationContext(), "Failure on downloading the image URL",Toast.LENGTH_LONG).show();
                        } else if (activity instanceof AddProductActivity){
                            //hide progress bar
                            Toast.makeText(activity.getApplicationContext(), "Failure on downloading the image URL",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });
    }

    public void uploadProductDetails(AddProductActivity activity, Product productInfo){
        fStore.collection(Constants.getPRODUCTS())
                .document()
                .set(productInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.productUploadSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
                System.out.println("Error while uploading the product details");
            }
        });
    }


    public void getProductList(Fragment fragment){
        fStore.collection(Constants.getPRODUCTS())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Product> products = new ArrayList<>();
                        for (DocumentSnapshot i : documents.getDocuments()) {
                            Product product = i.toObject(Product.class);
                            if (product != null){
                                product.setProduct_id(i.getId());
                            }
                            products.add(product);
                        }
                        if (fragment instanceof ProductsFragment){
                             ((ProductsFragment) fragment).successProductListFromFirestore(products);
                        }
                    }
                });
    }

    public void getProductDetails(ProductDetailsActivity activity, String productID){
        System.out.println(productID.toString() + "AWW");
        fStore.collection(Constants.getPRODUCTS())
                .document(productID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        Product product = document.toObject(Product.class);
                        product.setProduct_id(document.getId());

                        activity.productDetailsSuccess(product);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    //hide progress dialog
            }
        });
    }

    public void addCartItems(ProductDetailsActivity activity, CartItem cartItem){
        fStore.collection(Constants.getCartItems())
                .document()
                .set(cartItem, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.addToCartSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog

            }
        });
    }

    public void deleteProduct(ProductsFragment fragment, String productID){
        fStore.collection(Constants.getPRODUCTS())
                .document(productID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        fragment.productDeleteSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
                System.out.println("Error while deleting the product");
            }
        });
    }

    public void getCartList(Activity activity){
        fStore.collection(Constants.getCartItems())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<CartItem> cartList = new ArrayList<>();
                        for (DocumentSnapshot i : documents.getDocuments()){
                            CartItem cartItem = i.toObject(CartItem.class);
                            if (cartItem != null)
                            cartItem.setId(i.getId());

                            cartList.add(cartItem);
                        }

                        if (activity instanceof CartListActivity){
                            ((CartListActivity) activity).successCartItemsList(cartList);
                        } else if (activity instanceof CheckoutActivity){
                            ((CheckoutActivity) activity).successCartItemsList(cartList);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (activity instanceof CartListActivity){
                    //hide progress dialog
                    System.out.println("Error!");
                }
            }
        });
    }

    public void updateAddress(AddAddressActivity activity, Address addressInfo, String addressId){
        fStore.collection(Constants.getADDRESSES())
                .document(addressId)
                .set(addressInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.addUpdateAddressSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void getAddressesList(AddressListActivity activity){
        fStore.collection(Constants.getADDRESSES())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Address> addressList = new ArrayList<>();

                        for (DocumentSnapshot i : documents.getDocuments()){
                            Address address = i.toObject(Address.class);
                            address.setId(i.getId());

                            addressList.add(address);
                        }
                        activity.successAddressListFromFirestore(addressList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Hide progress dialog
            }
        });
    }

    public void updateAllDetails(CheckoutActivity activity, ArrayList<CartItem> cartList, Order order){
        WriteBatch writeBatch = fStore.batch();

        for (CartItem cartItem : cartList){
            SoldProduct soldProduct = new SoldProduct(
                cartItem.getProduct_ownerId(),
                cartItem.getTitle(),
                cartItem.getPrice(),
                cartItem.getCart_quantity(),
                cartItem.getImage(),
                    order.getTitle(),
                    order.getOrder_dateTime(),
                    order.getSubtotal(),
                    order.getShippingCharge(),
                    order.getTotalAmount(),
                    order.getAddress()
            );

            //Make an entry for sold product in cloud firestore.
            DocumentReference documentReference = fStore.collection(Constants.getSoldProducts())
                    .document(cartItem.getProduct_id());
            writeBatch.set(documentReference, soldProduct);
        }
        // Here we will update the product stock in the products collection based to cart quantity.
        for (CartItem cartItem : cartList){
            HashMap<String, Object> productHashMap = new HashMap<>();

             productHashMap.put(Constants.getStockQuantity(),
                        String.valueOf(Integer.parseInt(cartItem.getStock_quantity()) -Integer.parseInt(cartItem.getCart_quantity())));

             DocumentReference documentReference = fStore.collection(Constants.getPRODUCTS())
                                        .document(cartItem.getProduct_id());
             writeBatch.update(documentReference, productHashMap);
        }

        //after the update/purchase we want to empty/delete the cartItems from the collection
        for (CartItem cartItem : cartList){
            DocumentReference documentReference = fStore.collection(Constants.getCartItems())
                    .document(cartItem.getId());
            writeBatch.delete(documentReference);
        }

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //show progress dialog
                    activity.allDetailsUpdatedSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
            }
        });
    }

    public void getSoldProductsList(SoldProductsFragment fragment){
        fStore.collection(Constants.getSoldProducts())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<SoldProduct> soldProducts = new ArrayList<>();
                    for (DocumentSnapshot item : documents){
                        SoldProduct model = item.toObject(SoldProduct.class);
                        model.setId(item.getId());

                        soldProducts.add(model);
                        }
                        System.out.println(soldProducts.toString());
                        fragment.successSoldProductsList(soldProducts);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    //hide progress dialog
            }
        });

    }

    public void getOrdersList(OrdersFragment fragment){
        fStore.collection(Constants.getORDERS())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Order> ordersList = new ArrayList<>();
                        for (DocumentSnapshot items : documents){

                            Order model = items.toObject(Order.class);
                            model.setId(items.getId());

                            ordersList.add(model);
                        }
                        fragment.successOrderPlaced(ordersList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void placeOrder(CheckoutActivity activity, Order order){
            fStore.collection(Constants.getORDERS())
                    .document()
                    .set(order, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            activity.orderPlacedSuccess();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
    }


    public void getCurrentUserDetails(Activity activity){
        fStore.collection(Constants.getUSERS())
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        User user = document.toObject(User.class);
                        if (activity instanceof CheckoutActivity){
                            ((CheckoutActivity) activity).getUserDetailsSuccess(user);
                        } else if (activity instanceof DiscountCouponsActivity){
                            ((DiscountCouponsActivity) activity).successDetailsFromFirestore(user);
                        } else if (activity instanceof AddReviewActivity){
                            ((AddReviewActivity) activity).successGetUser(user);
                        }
                    }
                });
    }

    public void getCurrentDetailsV0(Activity activity){
        fStore.collection(Constants.getUSERS())
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                      User user = documentSnapshot.toObject(User.class);
                      if (activity instanceof AddMessageActivity){
                          ((AddMessageActivity) activity).getCurrentUserDetails(user);
                      } else if (activity instanceof MessageViewActivity) {
                          ((MessageViewActivity) activity).successGetUserV0(user);
                      }
                  }
              }
              );
    }

    public void getCurrentDetailsV1(Activity activity){
        fStore.collection(Constants.getUSERS())
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                      User user = documentSnapshot.toObject(User.class);
                      if (activity instanceof MessageViewActivity) {
                          ((MessageViewActivity) activity).successGetUserV1(user);
                      }
                  }
              });
    }

    public void getSenderUser(MessageViewActivity activity, String senderId){
        fStore.collection(Constants.getUSERS())
                .document(senderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        activity.successGetSenderUser(user);
                    }
                });
    }

    public void getReceiverUser(MessageViewActivity activity, String receiverId){
        fStore.collection(Constants.getUSERS())
                .document(receiverId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        activity.successGetReceiverUser(user);
                    }
                });
    }

    public void getProductOwnerDetails(Activity activity, String productOwnerId){
        fStore.collection(Constants.getUSERS())
                .document(productOwnerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        User user = document.toObject(User.class);
                        if (activity instanceof ProductDetailsActivity){
                            ((ProductDetailsActivity) activity).getUserDetails(user);
                        } else if (activity instanceof AddMessageActivity){
                            ((AddMessageActivity) activity).getUserDetails(user);
                        }

                    }
                });
    }


    public void setDiscountCouponForCurrentUser(DiscountCoupon discountCoupon, Context context){
        fStore.collection(Constants.getDISCOUNTCOUPONS())
                .document()
                .set(discountCoupon)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "You successfully got 1 discont coupon!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setPointForCurrentUser(Activity activity, HashMap<String, Object> userHashMap){
        fStore.collection(Constants.getUSERS())
                .document(getCurrentUserID())
                .set(userHashMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    public void getAllDiscountCoupons(ActivateCouponActivity activity){
        fStore.collection(Constants.getDISCOUNTCOUPONS())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<DiscountCoupon> discountCoupons = new ArrayList<>();
                        for (DocumentSnapshot i : documents){
                            DiscountCoupon discountCoupon = i.toObject(DiscountCoupon.class);

                            discountCoupons.add(discountCoupon);
                            discountCoupon.setId(i.getId());
                        }
                        activity.successGetDiscountsFromFirestore(discountCoupons);
                    }
                });
    }

    public void removeCouponPerUse(DiscountCoupon discountCoupon){
        fStore.collection(Constants.getDISCOUNTCOUPONS())
                .document(discountCoupon.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    public void getAllUsers(Activity activity){
        fStore.collection(Constants.getUSERS())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<User> users = new ArrayList<>();
                            for (DocumentSnapshot item : documents){
                                User user = item.toObject(User.class);
                                if (user != null)
                                users.add(user);
                            }
                            if (activity instanceof CheckoutActivity){
                                ((CheckoutActivity) activity).successGetUsersFromFirestore(users);
                            } else  if (activity instanceof PodiumActivity){
                                ((PodiumActivity) activity).successGetUsers(users);
                            }

                    }
                });
    }



    public void setPointsForDifferentUsers(HashMap<String, Object> userHasMap, User user){
        fStore.collection(Constants.getUSERS())
                .document(user.getId())
                .set(userHasMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println( "Userul " + user.getFirstName() + "a primit " + userHasMap.toString());
                    }
                });
    }


    public void deleteAddress(Context context, String address_id){
        fStore.collection(Constants.getADDRESSES())
                .document(address_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (context instanceof AddressListActivity){
                            ((AddressListActivity) context).itemRemovedSuccess();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public void addAddress(AddAddressActivity activity, Address addressInfo){
        fStore.collection(Constants.getADDRESSES())
                .document()
                .set(addressInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.successAddAddress();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void updateMyCart(Context context, String cart_id, HashMap<String, Object> itemHashMap){
        fStore.collection(Constants.getCartItems())
                .document(cart_id)
                .update(itemHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (context instanceof CartListActivity){
                            ((CartListActivity) context).itemUpdateSuccess();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (context instanceof CartListActivity){
                    //hide progress dialog
                    System.out.println("Error while updating the cart item.");
                }

            }
        });

    }

    public void checkIfItemExistInCart(ProductDetailsActivity activity, String productId){
        fStore.collection(Constants.getCartItems())
                .whereEqualTo(Constants.getUserId(), getCurrentUserID())
                .whereEqualTo(Constants.getProductId(), productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                       if (documents.size() > 0 ){
                           activity.productExistsInCart();
                       } else {
                           //hide progress dialog
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
            }
        });
    }

    public void removeItemFromCart(Context context, String cart_id){
        fStore.collection(Constants.getCartItems())
                .document(cart_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (context instanceof CartListActivity){
                            ((CartListActivity) context).itemRemovedSuccess();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (context instanceof CartListActivity){
                    //hide progress bar dialog;
                    System.out.println("Error while removing the item from the cart list.");
                }
            }
        });
    }

    public void getAllProductsList(Activity activity){
        fStore.collection(Constants.getPRODUCTS())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot i : documents.getDocuments()){
                            Product model = i.toObject(Product.class);
                            if (model != null)
                                model.setProduct_id(i.getId());

                                productList.add(model);
                        }
                        if (activity instanceof CartListActivity){
                           ((CartListActivity) activity).successProductsListFromFirestore(productList);
                        } else if (activity instanceof CheckoutActivity){
                            ((CheckoutActivity) activity).successProductsListFromFirestore(productList);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
            }
        });
    }

    public void getDashboardItemsList(DashboardFragment fragment){
        fStore.collection(Constants.getPRODUCTS())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot i : documents.getDocuments()) {
                            Product product = i.toObject(Product.class);

                            product.setProduct_id(i.getId());
                            productList.add(product);
                        }

                        fragment.successDashboardItemsList(productList);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
                System.out.println("Error while getting dashboard items list.");
            }
        });
    }

    public void getDashboardItemsListSorted(DashboardFragment fragment){
        fStore.collection(Constants.getPRODUCTS())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot i : documents.getDocuments()) {
                            Product product = i.toObject(Product.class);

                            product.setProduct_id(i.getId());
                            productList.add(product);
                        }

                        fragment.successDashboardItemsList(productList);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hide progress dialog
                System.out.println("Error while getting dashboard items list.");
            }
        });
    }

    public void addMessageToFirestore(AddMessageActivity activity, Message message){
        fStore.collection(Constants.getMESSAGES())
                .document()
                .set(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.successMessageSent();
                    }
                });
    }

    public void getReceivedMessageList(ReceivedMessagesFragment fragment){
        fStore.collection(Constants.getMESSAGES())
                .whereEqualTo(Constants.getReceiverId(),getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Message> messages = new ArrayList<>();
                        for (DocumentSnapshot items : documents){
                          Message message =  items.toObject(Message.class);
                            messages.add(message);
                            fragment.sucessGetReceivedMessages(messages);
                        }
                    }
                });
    }

    public void getSentMessageList(SentMessagesFragment fragment){
        fStore.collection(Constants.getMESSAGES())
                .whereEqualTo(Constants.getSenderId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Message> messages = new ArrayList<>();
                        for (DocumentSnapshot items: documents){
                            Message message = items.toObject(Message.class);
                            messages.add(message);
                            fragment.successGetSentMessageFromFirestore(messages);
                        }
                    }
                });
    }

    public void addReviewToFirestore(AddReviewActivity activity, Review review){
        fStore.collection(Constants.getREVIEWS())
                .document()
                .set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.successAddReview();
                    }
                });
    }


    public void getUserFromId(ProductOwnerProfileActivity activity, String user_id){
        fStore.collection(Constants.getUSERS())
                .document(user_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        activity.successGetUser(user);
                    }
                });
    }

    public void getUserVisitorDetails(ProductOwnerProfileActivity activity, String userId) {
            fStore.collection(Constants.getREVIEWS())
                    .whereEqualTo(Constants.getExtraReceiverId(), userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documents) {
                            ArrayList<Review> reviews = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : documents){
                                Review review = documentSnapshot.toObject(Review.class);
                                reviews.add(review);
                            }
                            activity.successGetReviews(reviews);
                        }
                    });
    }

    public void getReviewsForUser(SettingsActivity activity, String userId){
        fStore.collection(Constants.getREVIEWS())
                .whereEqualTo(Constants.getExtraReceiverId(), userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Review> reviews = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : documents){
                            Review review = documentSnapshot.toObject(Review.class);
                            reviews.add(review);
                        }
                        activity.successGetReviews(reviews);
                    }
                });
    }

    public void updateProduct(UpdateProductActivity activity, HashMap<String, Object> productHashMap, String productId){
        fStore.collection(Constants.getPRODUCTS())
                .document(productId)
                .update(productHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.productUpdateSuccess();
                    }
                });
    }

    public void updateFavoriteProduct(ProductDetailsActivity activity, HashMap<String, Object> productHashMap, String productId){
        fStore.collection(Constants.getPRODUCTS())
                .document(productId)
                .update(productHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.favoriteAddSuccess();
                    }
                });
    }

   public void updateProductFavoriteId(ProductDetailsActivity activity, HashMap<String, Object> productHashMap, String productId){
        fStore.collection(Constants.getPRODUCTS())
                .document(productId)
                .update(productHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
   }

   public void getFavoriteProducts(FavoritesActivity activity){
        fStore.collection(Constants.getPRODUCTS())
                .whereEqualTo(Constants.getFAVORITE(), "1")
                .whereEqualTo(Constants.getUserfavoriteId(), getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Product> products = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots){
                            Product product = document.toObject(Product.class);
                            product.setProduct_id(document.getId());
                            products.add(product);
                        }
                        activity.successGetFavProducts(products);
                    }
                });
   }

   public void getAllSoldProducts(DashboardFragment fragment){
        fStore.collection(Constants.getSoldProducts())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<SoldProduct> soldProducts = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : documents){
                            SoldProduct soldProduct = documentSnapshot.toObject(SoldProduct.class);
                            soldProduct.setId(documentSnapshot.getId());
                            soldProducts.add(soldProduct);
                        }
                        fragment.successGetAllSoldProducts(soldProducts);
                    }
                });
   }

   public void getAllUsersV2(PodiumActivity activity){
       fStore.collection(Constants.getUSERS())
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot documents) {
                       ArrayList<User> users = new ArrayList<>();
                       for (DocumentSnapshot item : documents){
                           User user = item.toObject(User.class);
                           if (user != null)
                               users.add(user);
                       }
                           ((PodiumActivity) activity).successGetUsersV2(users);
                   }
               });
   }

   public void getAllUsersV3(PodiumActivity activity){
       fStore.collection(Constants.getUSERS())
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot documents) {
                       ArrayList<User> users = new ArrayList<>();
                       for (DocumentSnapshot item : documents){
                           User user = item.toObject(User.class);
                           if (user != null)
                               users.add(user);
                       }
                           ((PodiumActivity) activity).successGetUsersV3(users);
                   }
               });
   }

   public void updateSoldProductsUser(String userId, HashMap<String, Object> userHashMap){
        fStore.collection(Constants.getUSERS())
                .document(userId)
                .update(userHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
   }

   public void getAllOrders(DashboardFragment fragment){
        fStore.collection(Constants.getORDERS())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        ArrayList<Order> orders = new ArrayList<>();
                        for (DocumentSnapshot document : documents){
                            Order order = document.toObject(Order.class);
                            orders.add(order);
                        }
                        fragment.successGetAllOrders(orders);
                    }
                });
   }

   public void updateOrdersUser(String userId, HashMap<String, Object> userHashMap){
        fStore.collection(Constants.getUSERS())
                .document(userId)
                .update(userHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
   }

    public void setCoordonatesForUser(HashMap<String, Object> hashMap){
            fStore.collection(Constants.getUSERS())
                    .document(getCurrentUserID())
                    .update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
    }

}
