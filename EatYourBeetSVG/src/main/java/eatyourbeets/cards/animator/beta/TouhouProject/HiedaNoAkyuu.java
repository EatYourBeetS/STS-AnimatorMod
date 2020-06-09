package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class HiedaNoAkyuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiedaNoAkyuu.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public HiedaNoAkyuu()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
        SetCostUpgrade(-1);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.Scry(999);
        GameActions.Top.MoveCards(player.hand, player.drawPile);
        GameActions.Top.MoveCards(player.discardPile, player.drawPile);

    }
}

