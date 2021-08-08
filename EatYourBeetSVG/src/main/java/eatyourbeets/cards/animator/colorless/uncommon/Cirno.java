package eatyourbeets.cards.animator.colorless.uncommon;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Cirno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cirno.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public Cirno()
    {
        super(DATA);

        Initialize(3, 0, 1);
        SetUpgrade(1, 0, 1);

        SetAffinity_Blue(1, 0, 1);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }

            GameActions.Top.Wait(0.3f);
        });

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT)
        .SetVFX(true, false)
        .SetDamageEffect((c, __) -> GameActions.Bottom.ApplyFreezing(player, c, magicNumber).ShowEffect(false, true));
    }
}

