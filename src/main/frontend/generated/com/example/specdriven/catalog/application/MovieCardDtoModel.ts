import { _getPropertyModel as _getPropertyModel_1, BooleanModel as BooleanModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type MovieCardDto_1 from "./MovieCardDto.js";
class MovieCardDtoModel<T extends MovieCardDto_1 = MovieCardDto_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(MovieCardDtoModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "java.lang.Long" } }));
    }
    get title(): StringModel_1 {
        return this[_getPropertyModel_1]("title", (parent, key) => new StringModel_1(parent, key, false, { meta: { javaType: "java.lang.String" } }));
    }
    get durationMinutes(): NumberModel_1 {
        return this[_getPropertyModel_1]("durationMinutes", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "java.lang.Integer" } }));
    }
    get posterUrl(): StringModel_1 {
        return this[_getPropertyModel_1]("posterUrl", (parent, key) => new StringModel_1(parent, key, false, { meta: { javaType: "java.lang.String" } }));
    }
    get hasUpcomingShows(): BooleanModel_1 {
        return this[_getPropertyModel_1]("hasUpcomingShows", (parent, key) => new BooleanModel_1(parent, key, false, { meta: { javaType: "boolean" } }));
    }
}
export default MovieCardDtoModel;
