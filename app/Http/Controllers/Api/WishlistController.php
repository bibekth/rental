<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Wishlist;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Throwable;

class WishlistController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'product_id' => 'required|exists:products,id',
        ]);

        $wishlist = Wishlist::updateOrCreate(
            [
                'user_id' => auth()->id(),
                'product_id' => $request->product_id
            ]
        );

        return response()->json(['message' => 'Product added to wishlist', 'data' => $wishlist], 201);
    }


    public function index()
    {
        try {
            $wishlists = Wishlist::with('product')->where('user_id', auth()->id())->get();

            return response()->json($wishlists);
            // return response()->json(['success'=>true,'data'=>$wishlists], 200);
        } catch (Throwable $e) {
            return response()->json(['success' => false, 'message' => $e->getMessage()], 500);
        }
    }


    public function destroy($productId)
    {
        $wishlist = Wishlist::where('user_id', auth()->id())->where('product_id', $productId)->firstOrFail();

        $wishlist->delete();

        return response()->json(['message' => 'Product removed from wishlist']);
    }

    public function checkWishlist(Request $request)
    {
        $check = Wishlist::where('user_id', Auth::id())->where('product_id', $request->product_id)->exists();
        if ($check) {
            return response()->json(['isLiked' => true], 200);
        } else {
            return response()->json(['isLiked' => false], 200);
        }
    }
}
