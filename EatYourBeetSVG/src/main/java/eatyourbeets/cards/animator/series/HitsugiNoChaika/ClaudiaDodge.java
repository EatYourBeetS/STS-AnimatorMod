package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class ClaudiaDodge extends AnimatorCard {
    public static final EYBCardData DATA = Register(ClaudiaDodge.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ClaudiaDodge() {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Dark();
        SetAffinity_Mind();
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(GameActionManager.totalDiscardedThisTurn <= 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        for (int i=0; i<secondaryValue; i++) {
            GameActions.Bottom.ChannelRandomCommonOrb(rng);
        }

        GameActions.Bottom.Draw(magicNumber);
    }
}