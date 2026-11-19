package com.ecommerce.product.dto;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorPageResponse<t> {
private List<t> content;
private Long nextCursor;
private boolean hasNext;
}
