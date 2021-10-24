package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.NextTurnCardCopyPower;
import eatyourbeets.utilities.GameActions;

public class MattheusCallaway extends AnimatorCard {
    public static final EYBCardData DATA = Register(MattheusCallaway.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public MattheusCallaway() {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 4, 0);

        SetAffinity_Earth();
        SetAffinity_Mind();
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainTemporaryArtifact(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter()) {
            GameActions.Bottom.SelectFromHand(name, 1, false)
                    .SetFilter(card -> card.type == CardType.SKILL && (card.rarity == CardRarity.COMMON || card.rarity == CardRarity.BASIC))
                    .AddCallback(cards -> {
                        if (cards.size() > 0) {
                            GameActions.Bottom.ApplyPower(new NextTurnCardCopyPower(player, cards.get(0)));
                        }
                    });
        }
    }
}