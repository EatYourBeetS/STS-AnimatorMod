package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.animator.tokens.AffinityToken_Green;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KonpakuYoumu extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(KonpakuYoumu.class).SetSkill(-1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Green), true));

    public KonpakuYoumu()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Green(1, 0, 0);

        SetAffinityRequirement(Affinity.Green, 6);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(0, 0, 0, 0);
            SetUpgrade(0, 0, 0, 0);
            SetRetain(true);
        }
        else {
            Initialize(0, 0, 0, 0);
            SetUpgrade(0, 0, 1, 0);
            SetRetain(false);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int xCost = GameUtilities.GetXCostEnergy(this) + magicNumber;
        GameActions.Bottom.Scry(xCost);
        GameActions.Bottom.DrawNextTurn(xCost);
        if (xCost > 2 && CheckAffinity(Affinity.Green)) {
            AffinityToken_Green token = new AffinityToken_Green();
            token.upgrade();
            GameActions.Bottom.MakeCardInHand(token);
        }
    }
}

