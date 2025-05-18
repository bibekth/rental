<?php

namespace App\Helper;

class ResponseHelper
{
    /**
     * Create a new class instance.
     */
    public function __construct()
    {
        
    }
    //response pathauna ho yo, yo chai sab lai kaam laghcha, if msg okay cha vani k garni, error cha vani k garni vanera dekhauna

    public static function success($status='success',$message=null,$data=[],$statusCode=200){
        return response()->json([
            'message' => $message,
            'data'=>$data,
            'status'=>$status,
        ],$statusCode);
    }
    public static function errors($status='error', $message=null,$statusCode= 400){
        return response()->json([
            'message' => $message,
            'status'=>$status,
        ],$statusCode);
    }
}
