<?php

namespace App\Http\Controllers;

use App\Models\Category;
use App\Models\Comment;
use App\Models\Order;
use App\Models\OrderDetails;
use App\Models\Product;
use App\Models\User;
use Illuminate\Http\Request;
use Throwable;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        // $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Contracts\Support\Renderable
     */
    public function index()
    {
        $product = Product::count();
        $category = Category::count();
        $user = User::count();
        $order = Order::count();
        $comment = Comment::count();
        $orderdetails = OrderDetails::count();
        return view('home', compact('product', 'category','user', 'order', 'comment', 'orderdetails'));
    }

    public function gitlabWebhook(Request $request) {
          try {
            $secret = 'monkey@21';
            $gitlabToken = $request->header('X-Gitlab-Token');

            if ($gitlabToken !== $secret) {
                return $this->sendError('Invalid Token', null, 403);
                // return response()->json('Invalid token', 403);
            }

            $data = $request->all();

            if (isset($data['ref']) && $data['ref'] === 'refs/heads/main') {
                $output = [];
                $returnCode = 0;

                // exec(`eval "$(ssh-agent -s)" && ssh-add ~/.ssh/merogrocery && cd ~/public_html/merogrocery && git pull origin development 2>&1`, $output, $returnCode);

                // file_put_contents("gitlab_webhook.log", implode("\n", $output) . "\n", FILE_APPEND);
            }

            return response()->json(['success'=>true, 'message'=>'success'], 200);
            // return response()->json('success', 200);
        } catch (Throwable $e) {
            return response()->json(['success'=>false, 'message'=>'internal error'], 500);
            // return response()->json($e->getMessage(), 500);
        }
    }
}
