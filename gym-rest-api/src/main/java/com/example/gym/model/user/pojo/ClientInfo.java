package com.example.gym.model.user.pojo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Client", description = "Сущность клиента")
public class ClientInfo {

    @Schema(name = "Баллы лояльности", description = "100")
    private Integer loyaltyPoints = 0;
    @ArraySchema(
           schema = @Schema(name = "Абонементы", implementation = Subscription.class)
    )
    private List<Subscription> subscriptions;
    @ArraySchema(
            schema = @Schema(name = "История транзаций баллов лояльности", implementation = loyaltyPointsEntry.class)
    )
    private List<loyaltyPointsEntry> loyaltyPointsHistory;

}
