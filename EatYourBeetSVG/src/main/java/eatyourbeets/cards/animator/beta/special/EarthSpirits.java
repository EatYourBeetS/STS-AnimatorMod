package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EarthSpirits extends AnimatorCard {
    public static final EYBCardData DATA = Register(EarthSpirits.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).PostInitialize(data -> data.AddPreview(new Haniwa(), false));

    public EarthSpirits() {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 2);
        SetAffinity_Orange(1, 0, 0);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.ExhaustFromPile(name, magicNumber, player.drawPile, player.hand)
                .SetOptions(true, true)
                .SetFilter(c -> GameUtilities.IsHindrance(c));

        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Haniwa());
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}