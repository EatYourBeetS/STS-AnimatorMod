package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class BattleDrones extends AnimatorRelic
{
    public static final String ID = CreateFullID(BattleDrones.class.getSimpleName());

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, 1);
            GameActionsHelper.DamageRandomEnemy(p, 1, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);

            this.flash();
        }
    }
}