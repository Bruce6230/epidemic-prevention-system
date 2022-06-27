package com.makiyo.dao;

import com.makiyo.pojo.TbFaceModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbFaceModelDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(TbFaceModel record);

    TbFaceModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbFaceModel record);

    int updateByPrimaryKey(TbFaceModel record);

    public String searchFaceModel(int userId);

    public void insert(TbFaceModel faceModel);

    public int deleteFaceModel(int userId);
}
