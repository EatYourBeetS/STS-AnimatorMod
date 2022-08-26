package eatyourbeets.resources.animatorClassic;

import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.JAX;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import eatyourbeets.cards.animator.curse.common.*;
import eatyourbeets.cards.animator.curse.special.Curse_AscendersBane;
import eatyourbeets.cards.animator.special.Khajiit;
import eatyourbeets.cards.animator.special.SogaNoTojiko;
import eatyourbeets.cards.animator.special.Special_Miracle;
import eatyourbeets.cards.animator.special.Special_Refrain;
import eatyourbeets.cards.animator.status.*;
import eatyourbeets.cards.animatorClassic.basic.ImprovedDefend;
import eatyourbeets.cards.animatorClassic.basic.ImprovedStrike;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.AbstractCardLibrary;
import eatyourbeets.resources.GR;

import java.util.HashMap;

public class AnimatorClassicCardLibrary extends AbstractCardLibrary
{
    @Override
    protected void InitializeMap(HashMap<String, EYBCardData> map)
    {
        map.put(AscendersBane.ID, Curse_AscendersBane.DATA);
        map.put(VoidCard.ID, Status_Void.DATA);
        map.put(Slimed.ID, Status_Slimed.DATA);
        map.put(Wound.ID, Status_Wound.DATA);
        map.put(Dazed.ID, Status_Dazed.DATA);
        map.put(Burn.ID, Status_Burn.DATA);
        map.put(Apparition.ID, SogaNoTojiko.DATA);
        map.put(Miracle.ID, Special_Miracle.DATA);
        map.put(RitualDagger.ID, Khajiit.DATA);
        map.put(JAX.ID, Special_Refrain.DATA);
        map.put(Clumsy.ID, Curse_Clumsy.DATA);
        map.put(Decay.ID, Curse_Decay.DATA);
        map.put(Doubt.ID, Curse_Doubt.DATA);
        map.put(Injury.ID, Curse_Injury.DATA);
        map.put(Normality.ID, Curse_Normality.DATA);
        map.put(Pain.ID, Curse_Pain.DATA);
        map.put(Parasite.ID, Curse_Parasite.DATA);
        map.put(Regret.ID, Curse_Regret.DATA);
        map.put(Shame.ID, Curse_Shame.DATA);
        map.put(Writhe.ID, Curse_Writhe.DATA);
    }

    @Override
    public EYBCardData GetCardData(String cardID)
    {
        if (cardID.startsWith(GR.Animator.Prefix))
        {
            final EYBCardData data = EYBCard.GetStaticData(GR.AnimatorClassic.ConvertID(cardID, true));
            if (data == null)
            {
                if (cardID.contains("Strike_"))
                {
                    return ImprovedStrike.DATA;
                }
                if (cardID.contains("Defend_"))
                {
                    return ImprovedDefend.DATA;
                }
            }

            return data;
        }

        return super.GetCardData(cardID);
    }
}
