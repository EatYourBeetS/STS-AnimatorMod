package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnEndOfTurnSubscriber;
import patches.AbstractEnums;

public class MumenRider extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(MumenRider.class.getSimpleName());

    public MumenRider()
    {
        super(ID, 0, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(1, 0);

        this.purgeOnUse = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        else
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SMASH);
        }

        GameActionsHelper.DrawCard(p, 1);

        this.purgeOnUse = true;

        if (!tags.contains(AbstractEnums.CardTags.TEMPORARY))
        {
            PlayerStatistics.onEndOfTurn.Subscribe(this);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.player.drawPile.addToTop(this.makeStatEquivalentCopy());
        PlayerStatistics.onEndOfTurn.Unsubscribe(this);
    }
}