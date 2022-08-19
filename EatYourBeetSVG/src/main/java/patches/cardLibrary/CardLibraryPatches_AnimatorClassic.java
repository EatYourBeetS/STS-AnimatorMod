package patches.cardLibrary;

import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.colorless.JAX;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import eatyourbeets.cards.animator.curse.common.*;
import eatyourbeets.cards.animator.curse.special.Curse_AscendersBane;
import eatyourbeets.cards.animator.special.*;
import eatyourbeets.cards.animator.status.*;
import eatyourbeets.cards.animatorClassic.basic.ImprovedDefend;
import eatyourbeets.cards.animatorClassic.basic.ImprovedStrike;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public class CardLibraryPatches_AnimatorClassic
{
    public static EYBCardData GetReplacement(String cardID)
    {
        if (cardID.startsWith(GR.Animator.Prefix))
        {
            final EYBCardData data = AnimatorClassicCard.GetStaticData(GR.AnimatorClassic.ConvertID(cardID, true));
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

        switch (cardID)
        {
            case AscendersBane.ID: return Curse_AscendersBane.DATA;

            case VoidCard.ID: return Status_Void.DATA;
            case Slimed.ID: return Status_Slimed.DATA;
            case Wound.ID: return Status_Wound.DATA;
            case Dazed.ID: return Status_Dazed.DATA;
            case Burn.ID: return Status_Burn.DATA;

            case Apparition.ID: return SogaNoTojiko.DATA;
            case Miracle.ID: return Special_Miracle.DATA;
            case RitualDagger.ID: return Khajiit.DATA;
            case JAX.ID: return Special_Refrain.DATA;
            case Bite.ID: return Special_Bite.DATA;

            case Clumsy.ID: return Curse_Clumsy.DATA;
            case Decay.ID: return Curse_Decay.DATA;
            case Doubt.ID: return Curse_Doubt.DATA;
            case Injury.ID: return Curse_Injury.DATA;
            case Normality.ID: return Curse_Normality.DATA;
            case Pain.ID: return Curse_Pain.DATA;
            case Parasite.ID: return Curse_Parasite.DATA;
            case Regret.ID: return Curse_Regret.DATA;
            case Shame.ID: return Curse_Shame.DATA;
            case Writhe.ID: return Curse_Writhe.DATA;

            default: return null;
        }
    }
}
