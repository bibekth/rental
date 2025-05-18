<?php

namespace App\Http\Controllers\Api;

use App\Helper\ResponseHelper;
use App\Http\Controllers\Controller;
use App\Models\Comment;
use Exception;
use Illuminate\Http\Request;

class CommentController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'content' => 'required|string|max:255',
            'product_id' => 'required|exists:products,id',
        ]);

        try {
            $comment = new Comment();
            $comment->user_id = auth()->id();
            $comment->product_id = $request->product_id;
            $comment->content = $request->content;
            $comment->save();

            return ResponseHelper::success(message: 'Comment posted successfully!', data: $comment, statusCode: 201);
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Unable to save comment: ' . $ex->getMessage(), statusCode: 500);
        }
    }

    public function index($product_id)
    {
        $comments = Comment::with(['user', 'product'])
        ->where('product_id', $product_id)
        ->latest()
        ->get();

        if ($comments->isNotEmpty()) {
            return ResponseHelper::success(message: 'All Comments', data: $comments, statusCode: 200);
        } else {
            return ResponseHelper::success(message: 'No comments found', data: [], statusCode: 404);
        }
    }
    public function update(Request $request, $id)
{
    $request->validate([
        'content' => 'required|string|max:255',
    ]);

    try {
        $comment = Comment::findOrFail($id);

        $comment->content = $request->content;
        $comment->save();

        return response()->json([
            'success' => true,
            'message' => 'Comment updated successfully',
            'data' => $comment,
        ], 200);
    } catch (Exception $ex) {
        return response()->json([
            'success' => false,
            'message' => 'Unable to update comment: ' . $ex->getMessage(),
        ], 500);
    }
}

    public function destroy($id)
    {
        try {
            $comment = Comment::findOrFail($id);

            // Check if the authenticated user is the owner of the comment or an admin
            if (auth()->id() === $comment->user_id || auth()->user()->role === 'admin') {
                $comment->delete();
                return ResponseHelper::success(message: 'Comment deleted successfully', data: null, statusCode: 200);
            }

            return ResponseHelper::errors(message: 'Unauthorized', statusCode: 403);
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Unable to delete comment: ' . $ex->getMessage(), statusCode: 500);
        }
    }


}
