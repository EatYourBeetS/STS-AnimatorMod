package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ReisenInaba extends AnimatorCard implements Hidden //TODO
{
    public static final EYBCardData DATA = Register(ReisenInaba.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ReisenInaba()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new CreateRandomCurses(1, p.hand)).AddCallback(card -> {
            GameActions.Bottom.ApplyPower(new ReisenInabaPower(player, card));
        });
    }

    public static class ReisenInabaPower extends AnimatorPower
    {
        public AbstractCard card;

        public ReisenInabaPower(AbstractPlayer owner, AbstractCard card)
        {
            super(owner, ReisenInaba.DATA);

            this.card = card;
            updateDescription();
        }



    }
}

