package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.po.Category;
import com.frontendAttic.entity.query.CategoryQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类 Controller
 */
@RestController("categoryController")
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Resource
    private CategoryService categoryService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadAllCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_CATEGORY)
    public ResponseVO loadAllCategory(CategoryQuery query) {
        query.setOrderBy("sort asc");
        return createSuccessResponse(categoryService.findListByParam(query));
    }

    @RequestMapping("/saveCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_CATEGORY)
    public ResponseVO saveCategory(Category category) {
        categoryService.saveCategory(category);
        return createSuccessResponse(null);
    }


    @RequestMapping("/delCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.DELETE_CATEGORY)
    public ResponseVO delCategory(@VerifyParam(required = true) Integer categoryId) {
        categoryService.deleteCategoryByCategoryId(categoryId);
        return createSuccessResponse(null);
    }

    @RequestMapping("/changeSort")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.DELETE_CATEGORY)
    public ResponseVO changeSort(@VerifyParam(required = true) String categoryIds) {
        categoryService.changeSort(categoryIds);
        return createSuccessResponse(null);
    }

    @RequestMapping("/loadAllCategory4Select")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_CATEGORY)
    public ResponseVO loadAllCategory4Select(@VerifyParam(required = true) Integer type) {
        List<Category> list = categoryService.loadAllCategoryByType(type);
        return createSuccessResponse(list);
    }
}