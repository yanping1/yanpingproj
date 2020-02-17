package com.dkha.common.entity.vo.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FScoreCompVO {
	private String faceId;
	private String comparisonFaceId;
	private Double score;
}
