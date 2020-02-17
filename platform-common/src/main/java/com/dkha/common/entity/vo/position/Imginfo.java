package com.dkha.common.entity.vo.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Imginfo {

	private List<String> faceIds;
	private Integer reSizeH; 
	private Integer reSizeW;
}
