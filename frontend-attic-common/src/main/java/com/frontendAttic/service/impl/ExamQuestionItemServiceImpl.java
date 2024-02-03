package com.frontendAttic.service.impl;


import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.po.ExamQuestionItem;
import com.frontendAttic.entity.query.ExamQuestionItemQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.mappers.ExamQuestionItemMapper;
import com.frontendAttic.service.ExamQuestionItemService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *  业务接口实现
 */
@Service("examQuestionItemService")
public class ExamQuestionItemServiceImpl implements ExamQuestionItemService {

	@Resource
	private ExamQuestionItemMapper<ExamQuestionItem, ExamQuestionItemQuery> examQuestionItemMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ExamQuestionItem> findListByParam(ExamQuestionItemQuery param) {
		return this.examQuestionItemMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ExamQuestionItemQuery param) {
		return this.examQuestionItemMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PageResultVO<ExamQuestionItem> findListByPage(ExamQuestionItemQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ExamQuestionItem> list = this.findListByParam(param);
		PageResultVO<ExamQuestionItem> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ExamQuestionItem bean) {
		return this.examQuestionItemMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ExamQuestionItem> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.examQuestionItemMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ExamQuestionItem> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.examQuestionItemMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(ExamQuestionItem bean, ExamQuestionItemQuery param) {
		StringUtil.checkParam(param);
		return this.examQuestionItemMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ExamQuestionItemQuery param) {
		StringUtil.checkParam(param);
		return this.examQuestionItemMapper.deleteByParam(param);
	}

	/**
	 * 根据ItemId获取对象
	 */
	@Override
	public ExamQuestionItem getExamQuestionItemByItemId(Integer itemId) {
		return this.examQuestionItemMapper.selectByItemId(itemId);
	}

	/**
	 * 根据ItemId修改
	 */
	@Override
	public Integer updateExamQuestionItemByItemId(ExamQuestionItem bean, Integer itemId) {
		return this.examQuestionItemMapper.updateByItemId(bean, itemId);
	}

	/**
	 * 根据ItemId删除
	 */
	@Override
	public Integer deleteExamQuestionItemByItemId(Integer itemId) {
		return this.examQuestionItemMapper.deleteByItemId(itemId);
	}
}