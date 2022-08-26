package eatyourbeets.resources;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCardLibrary
{
    protected final HashMap<String, EYBCardData> map = new HashMap<>();
    protected AbstractResources resources;

    protected abstract void InitializeMap(HashMap<String, EYBCardData> map);

    public void Initialize(AbstractResources resources)
    {
        this.resources = resources;

        InitializeMap(map);

        for (Map.Entry<String, EYBCardData> pair : map.entrySet())
        {
            pair.getValue().SharedID = pair.getKey();
        }
    }

    public String GetBaseID(String id)
    {
        final EYBCardData data = GetCardData(id);
        return data != null ? data.SharedID : id;
    }

    public EYBCardData GetCardData(String cardID)
    {
        final EYBCardData data = map.get(cardID);
        return data != null ? data : EYBCard.GetStaticData(cardID);
    }
}
