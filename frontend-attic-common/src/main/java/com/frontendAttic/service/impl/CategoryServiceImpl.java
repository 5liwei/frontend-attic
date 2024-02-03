package com.frontendAttic.service.impl;

import com.frontendAttic.entity.enums.CategoryTypeEnum;
import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.po.Category;
import com.frontendAttic.entity.query.CategoryQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.mappers.CategoryMapper;
import com.frontendAttic.service.CategoryService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类 业务接口实现
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper<Category, CategoryQuery> categoryMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Category> findListByParam(CategoryQuery param) {
        return categoryMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(CategoryQuery param) {
        return categoryMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<Category> findListByPage(CategoryQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Category> list = this.findListByParam(param);
        PageResultVO<Category> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Category bean) {
        return categoryMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return categoryMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return categoryMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Category bean, CategoryQuery param) {
        StringUtil.checkParam(param);
        return categoryMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(CategoryQuery param) {
        StringUtil.checkParam(param);
        return categoryMapper.deleteByParam(param);
    }

    /**
     * 根据CategoryId获取对象
     */
    @Override
    public Category getCategoryByCategoryId(Integer categoryId) {
        return categoryMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据CategoryId修改
     */
    @Override
    public Integer updateCategoryByCategoryId(Category bean, Integer categoryId) {
        return categoryMapper.updateByCategoryId(bean, categoryId);
    }

    /**
     * 根据CategoryId删除
     */
    @Override
    public Integer deleteCategoryByCategoryId(Integer categoryId) {
        return categoryMapper.deleteByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(Category category) {
        if (null == category.getCategoryId()) {
            CategoryQuery categoryQuery = new CategoryQuery();
            Integer count = categoryMapper.selectCount(categoryQuery);
            category.setSort(count + 1);
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateByCategoryId(category, category.getCategoryId());
        }

        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setCategoryName(category.getCategoryName());
        categoryQuery.setType(category.getType());
        Integer count = categoryMapper.selectCount(categoryQuery);
        if (count > 1) {
            throw new BusinessException("分类名称重复");
        }
        if (null == category.getCategoryName()) {
            return;
        }
        Category dbInfo = categoryMapper.selectByCategoryId(category.getCategoryId());
        if (!dbInfo.getCategoryName().equals(category.getCategoryName())) {
            categoryMapper.updateCategoryName(category.getCategoryName(), category.getCategoryId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSort(String categoryIds) {
        String[] categoryIdArray = categoryIds.split(",");
        Integer index = 1;
        for (String categoryIdStr : categoryIdArray) {
            Integer cateoryId = Integer.parseInt(categoryIdStr);
            Category category = new Category();
            category.setSort(index);
            categoryMapper.updateByCategoryId(category, cateoryId);
            index++;
        }
    }

    @Override
    public List<Category> loadAllCategoryByType(Integer type) {
        CategoryTypeEnum typeEnum = CategoryTypeEnum.getByType(type);
        if (typeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setOrderBy("sort asc");
        categoryQuery.setTypes(new Integer[]{typeEnum.getType(), CategoryTypeEnum.QUESTION_EXAM.getType()});
        return categoryMapper.selectList(categoryQuery);
    }
}