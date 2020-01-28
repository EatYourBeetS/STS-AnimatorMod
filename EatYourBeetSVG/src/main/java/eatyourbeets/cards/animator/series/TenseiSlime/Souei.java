package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Souei extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Souei.class, EYBCardBadge.Special);

    public Souei()
    {
        super(ID, 2, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.Callback(__ ->
        {
            AbstractPlayer player = AbstractDungeon.player;
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                PoisonPower poison = (PoisonPower) enemy.getPower(PoisonPower.POWER_ID);
                if (poison != null)
                {
                    GameActions.Top.Callback(new PoisonLoseHpAction(enemy, player, poison.amount, AbstractGameAction.AttackEffect.POISON))
                    .AddCallback(GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID), (intangible, action) ->
                    {
                        if (GameUtilities.TriggerOnKill(action.target, true))
                        {
                            if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) == (int) intangible)
                            {
                                GameActions.Top.StackPower(new IntangiblePlayerPower(AbstractDungeon.player, 1));
                            }
                        }
                    });
                }
            }
        });
    }
}