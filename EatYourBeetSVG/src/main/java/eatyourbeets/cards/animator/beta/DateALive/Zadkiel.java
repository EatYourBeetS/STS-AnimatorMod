package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class Zadkiel extends AnimatorCard implements Hidden {
    public static final EYBCardData DATA = Register(Zadkiel.class).SetSkill(2, CardRarity.SPECIAL);
    private static final int BLOCK_MULTIPLIER = 4;

    static
    {
        DATA.AddPreview(new YoshinoHimekawa(), true);
    }

    public Zadkiel() {
        super(DATA);

        Initialize(0, 4);
        SetCostUpgrade(1);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(BLOCK_MULTIPLIER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<BLOCK_MULTIPLIER; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }
    }
}