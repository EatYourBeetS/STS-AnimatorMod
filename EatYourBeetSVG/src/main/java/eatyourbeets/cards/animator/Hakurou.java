package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DrawAndUpgradeCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Hakurou extends AnimatorCard //implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(Hakurou.class.getSimpleName());

    public Hakurou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(1,0, 3);

        baseSecondaryValue = secondaryValue = 1;

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + PlayerStatistics.GetDexterity(player));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        AbstractPlayer p = AbstractDungeon.player;
        PlayerStatistics.ApplyTemporaryDexterity(p, p, secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new VFXAction(new DieDieDieEffect()));
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
        }

        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new DrawAndUpgradeCardAction(p, 1));
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}