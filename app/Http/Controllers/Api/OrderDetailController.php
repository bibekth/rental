<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\OrderDetails;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class OrderDetailController extends Controller
{

   
    public function store(Request $request)
    {
        // $request->validate([
        //     'order_id' => 'required|exists:orders,id',
        //     'deliveryaddress' => 'required|string',
        //     'payment_method' => 'required|string',
        //     'mobilenumber' => 'required|string',
        //     'statuspayment' => 'nullable|boolean',
        //     'discount' => 'nullable|numeric',
        //     'deliverystatus' => 'nullable|boolean',
        // ]);
        $validator = Validator::make($request->all(),[
            'order_id' => 'required|exists:orders,id',
            'deliveryaddress' => 'required|string',
            'payment_method' => 'required|string',
            'mobilenumber' => 'required|string',
            'statuspayment' => 'nullable|boolean',
            'discount' => 'nullable|numeric',
            'deliverystatus' => 'nullable|boolean',
            ]);
        if($validator->fails()){
            return response()->json(["error"=>$validator->errors()], 422);
        }
        $orderDetail = OrderDetails::create($request->all());

        return response()->json($orderDetail, 201);
    }

    public function show(string $id)
    {
        $orderDetail = OrderDetails::with('order')->findOrFail($id);
        return response()->json($orderDetail);
    }

    public function edit(string $id)
    {
        
    }

    public function update(Request $request, string $id)
    {
        $request->validate([
            'order_id' => 'nullable|exists:orders,id',
            'discount' => 'nullable|numeric',
            'deliveryaddress' => 'nullable|string',
            'payment_method' => 'nullable|string',
            'statuspayment' => 'nullable|boolean',
            'mobilenumber' => 'nullable|string',
            'deliverystatus' => 'nullable|boolean',
        ]);

        $orderDetail = OrderDetails::findOrFail($id);
        $orderDetail->update($request->all());

        return response()->json($orderDetail);
    }

    public function destroy(string $id)
    {
        $orderDetail = OrderDetails::findOrFail($id);
        $orderDetail->delete();

        return response()->json(['message' => 'Order Detail deleted successfully.']);
    }
}
