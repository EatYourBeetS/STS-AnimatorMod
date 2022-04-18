package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.MamiTomoe;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnLoseHPSubscriber;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MamiTomoe_Candeloro extends AnimatorCard implements OnLoseHPSubscriber
{
    public static final EYBCardData DATA = Register(MamiTomoe_Candeloro.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetSeries(MamiTomoe.DATA.Series);

    public MamiTomoe_Candeloro()
    {
        super(DATA);

        Initialize(0, 18, 1);
        SetUpgrade(0, 4, 0);

        SetAffinity_Dark(2);
        SetAffinity_Light(2);
        SetAffinity_Star(0, 0, 1);

        SetRetainOnce(true);
        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddStrength(magicNumber);
        }
    }

    @Override
    public void OnLoseHP(DamageInfo info, int amount)
    {
        if (player.exhaustPile.contains(this) && info.type == DamageInfo.DamageType.NORMAL && amount > 0)
        {
            SetExhaust(false);
            SetPurge(true, true);
            GameActions.Top.MoveCard(this, player.exhaustPile, player.hand).ShowEffect(true, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Strength, magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onLoseHP.Subscribe(this);
    }
}