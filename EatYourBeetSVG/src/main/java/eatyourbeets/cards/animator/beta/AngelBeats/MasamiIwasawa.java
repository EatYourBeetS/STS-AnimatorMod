package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MasamiIwasawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MasamiIwasawa.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self);

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 15, 2, 2);
        SetUpgrade(0, 5, 0, 0);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), secondaryValue, false, true));
        if (HasSynergy() && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.Draw(magicNumber);
        }
    }
}