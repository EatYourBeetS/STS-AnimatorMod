package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.series.MadokaMagica.MadokaKaname;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class MadokaKaname_KriemhildGretchen extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname_KriemhildGretchen.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetSeries(MadokaKaname.DATA.Series);

    public MadokaKaname_KriemhildGretchen()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Dark(2);
        SetAffinity_Light(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Dark::new, magicNumber);
        GameActions.Bottom.StackPower(new MadokaKaname_KriemhildGretchenPower(p, secondaryValue));
    }

    public static class MadokaKaname_KriemhildGretchenPower extends AnimatorPower
    {
        public MadokaKaname_KriemhildGretchenPower(AbstractCreature owner, int amount)
        {
            super(owner, MadokaKaname_KriemhildGretchen.DATA);

            Initialize(amount);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (card.type == CardType.CURSE)
            {
                GameActions.Bottom.GainTemporaryHP(amount);

                for (AbstractOrb orb : player.orbs)
                {
                    if (orb != null && Dark.ORB_ID.equals(orb.ID))
                    {
                        GameActions.Bottom.TriggerOrbPassive(orb, 1);
                    }
                }

                flashWithoutSound();
            }
        }
    }
}