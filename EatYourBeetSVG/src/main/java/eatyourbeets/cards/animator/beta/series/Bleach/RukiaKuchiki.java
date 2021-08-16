package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.beta.special.RukiaBankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class RukiaKuchiki extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(RukiaKuchiki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new RukiaBankai(), false);
    }

    public RukiaKuchiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 3);
        SetAffinity_Green(1, 1, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (IsStarter())
        {
            GameActions.Bottom.InduceOrb(new Frost());
        }

        GameActions.Bottom.ApplyFreezing(player, m, magicNumber);

        if (HasSynergy())
        {
            boolean hasEmptyOrbs = false;

            for (AbstractOrb orb : player.orbs)
            {
                if (orb instanceof EmptyOrbSlot)
                {
                    hasEmptyOrbs = true;
                    break;
                }
            }

            if (!hasEmptyOrbs)
            {
                GameActions.Bottom.MakeCardInDrawPile(new RukiaBankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        }
    }
}