<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class ProductUploadRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        if(request()->isMethod('PUT')){
            $rules= 'file|mimes:png,jpg,jpeg,webp';
        }
        elseif(request()->isMethod('POST')){
            $rules= 'required|file|mimes:png,jpg,jpeg,webp' ;

        }
        return [
            'name' => 'required|string|max:255',
            'description' => 'required|string',
            'amount' => 'required|numeric',
            'photo' => $rules,
            'purchase_date' => 'nullable|date',
            // 'image' => 'nullable|image|mimes:jpeg,png,jpg,gif,svg|max:2048', // Add validation for image
            'category_id' => 'required|exists:categories,id',
        ];
    }
    public function messages() : array{
        return [
           'name.required' => 'Product name is required!',
            'name.string' => 'Product name must be a string.',
            'name.max' => 'Product name cannot exceed 255 characters.',
            'description.required' => 'Product description is required!',
            'description.string' => 'Product description must be a string.',
            'amount.required' => 'Amount is required!',
            'amount.numeric' => 'Amount must be a numeric value.',
            'amount.min' => 'Amount must be at least 0.',
            'photo.file' => 'Image is required.',
            'photo.mimes' => 'The image must be of type: png, jpg, jpeg, webp.',
            'purchase_date.date' => 'Purchase date must be a valid date.',
            'category_id.required' => 'Category ID is required!',
            'category_id.exists' => 'The selected category ID does not exist.',
        ];
    }
}
